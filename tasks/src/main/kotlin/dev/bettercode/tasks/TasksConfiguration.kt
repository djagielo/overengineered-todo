@file:Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused")

package dev.bettercode.tasks

import dev.bettercode.tasks.application.projects.ProjectAssignmentService
import dev.bettercode.tasks.application.projects.ProjectCompletionService
import dev.bettercode.tasks.application.projects.ProjectService
import dev.bettercode.tasks.application.tasks.TaskCompletionService
import dev.bettercode.tasks.application.tasks.TaskService
import dev.bettercode.tasks.domain.projects.ProjectRepository
import dev.bettercode.tasks.domain.tasks.TasksRepository
import dev.bettercode.tasks.infra.adapter.db.inmemory.InMemoryProjectRepository
import dev.bettercode.tasks.infra.adapter.db.inmemory.InMemoryProjectsQueryRepository
import dev.bettercode.tasks.infra.adapter.db.inmemory.InMemoryQueryRepository
import dev.bettercode.tasks.infra.adapter.db.inmemory.InMemoryTasksRepository
import dev.bettercode.tasks.query.ProjectsQueryService
import dev.bettercode.tasks.query.TasksQueryRepository
import dev.bettercode.tasks.query.TasksQueryService
import dev.bettercode.tasks.shared.InMemoryEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TasksConfiguration {
    companion object {
        fun tasksFacade(inMemoryEventPublisher: InMemoryEventPublisher = InMemoryEventPublisher()): TasksFacade {
            val taskRepo = InMemoryTasksRepository()
            val projectRepo = InMemoryProjectRepository()
            val tasksQueryRepository = InMemoryQueryRepository(taskRepo)
            val projectsQueryRepository = InMemoryProjectsQueryRepository(projectRepo)
            val projectCrudService = ProjectService(projectRepo)
            val projectsQueryService = ProjectsQueryService(projectsQueryRepository)
            return TasksFacade(
                TaskService(taskRepo, projectCrudService),
                TaskCompletionService(taskRepo),
                projectCrudService,
                ProjectAssignmentService(projectRepo, taskRepo, inMemoryEventPublisher),
                ProjectCompletionService(projectRepo),
                TasksQueryService(tasksQueryRepository, taskRepo),
                projectsQueryService
            )
        }
    }

    @Bean
    internal fun taskRepository(): InMemoryTasksRepository {
        return InMemoryTasksRepository()
    }

    @Bean
    internal fun projectsRepository(): InMemoryProjectRepository {
        return InMemoryProjectRepository()
    }

    @Bean
    internal fun tasksQueryRepository(inMemoryTasksRepository: InMemoryTasksRepository): InMemoryQueryRepository {
        return InMemoryQueryRepository(inMemoryTasksRepository)
    }

    @Bean
    internal fun tasksFacade(
        tasksUseCase: TaskService,
        tasksCompletionService: TaskCompletionService,
        projectService: ProjectService,
        projectAssignmentService: ProjectAssignmentService,
        projectCompletionService: ProjectCompletionService,
        tasksQueryService: TasksQueryService,
        projectsQueryService: ProjectsQueryService
    ): TasksFacade {
        return TasksFacade(
            taskService = tasksUseCase,
            taskCompletionService = tasksCompletionService,
            projectService = projectService,
            projectAssignmentService = projectAssignmentService,
            projectCompletionService = projectCompletionService,
            tasksQueryService = tasksQueryService,
            projectsQueryService = projectsQueryService
        )
    }

    @Bean
    internal fun tasksCrudService(
        tasksRepository: TasksRepository,
        projectService: ProjectService
    ): TaskService {
        return TaskService(tasksRepository, projectService)
    }

    @Bean
    internal fun projectsCrudService(
        tasksRepository: TasksRepository,
        projectRepository: ProjectRepository
    ): ProjectService {
        return ProjectService(projectRepository)
    }

    @Bean
    internal fun projectsAssignmentService(
        projectRepository: ProjectRepository,
        tasksRepository: TasksRepository
    ): ProjectAssignmentService {
        return ProjectAssignmentService(projectRepository, tasksRepository, InMemoryEventPublisher())
    }

    @Bean
    internal fun taskCompletionService(tasksRepository: TasksRepository): TaskCompletionService {
        return TaskCompletionService(tasksRepository)
    }

    @Bean
    internal fun tasksQueryService(
        tasksQueryRepository: TasksQueryRepository,
        tasksRepository: TasksRepository
    ): TasksQueryService {
        return TasksQueryService(tasksQueryRepository, tasksRepository)
    }

    @Bean
    internal fun projectCompletionService(projectRepository: ProjectRepository): ProjectCompletionService {
        return ProjectCompletionService(projectRepository)
    }
}
