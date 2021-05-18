package dev.bettercode.tasks

import dev.bettercode.tasks.application.projects.ProjectAssignmentService
import dev.bettercode.tasks.application.projects.ProjectCrudService
import dev.bettercode.tasks.application.tasks.TaskCompletionService
import dev.bettercode.tasks.application.tasks.TaskCrudService
import dev.bettercode.tasks.domain.projects.ProjectRepository
import dev.bettercode.tasks.infra.adapter.db.InMemoryProjectRepository
import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.domain.tasks.TasksRepository
import dev.bettercode.tasks.infra.adapter.db.InMemoryTasksRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TasksConfiguration {
    companion object {
        fun tasksFacade(): TasksFacade {
            val taskRepo = InMemoryTasksRepository()
            val projectRepo = InMemoryProjectRepository()
            val projectCrudService = ProjectCrudService(projectRepo)
            return TasksFacade(
                TaskCrudService(taskRepo, projectCrudService),
                TaskCompletionService(taskRepo),
                projectCrudService,
                ProjectAssignmentService(projectRepo, taskRepo),
                TasksQueryService(taskRepo)
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
    internal fun tasksFacade(
        tasksCrudUseCase: TaskCrudService,
        tasksCompletionService: TaskCompletionService,
        projectCrudService: ProjectCrudService,
        projectAssignmentService: ProjectAssignmentService,
        tasksQueryService: TasksQueryService
    ): TasksFacade {
        return TasksFacade(
            tasksCrudUseCase,
            tasksCompletionService,
            projectCrudService,
            projectAssignmentService,
            tasksQueryService
        )
    }

    @Bean
    internal fun tasksCrudService(
        tasksRepository: TasksRepository,
        projectCrudService: ProjectCrudService
    ): TaskCrudService {
        return TaskCrudService(tasksRepository, projectCrudService)
    }

    @Bean
    internal fun projectsCrudService(
        tasksRepository: TasksRepository,
        projectRepository: ProjectRepository
    ): ProjectCrudService {
        return ProjectCrudService(projectRepository)
    }

    @Bean
    internal fun projectsAssignmentService(
        projectRepository: ProjectRepository,
        tasksRepository: TasksRepository
    ): ProjectAssignmentService {
        return ProjectAssignmentService(projectRepository, tasksRepository)
    }

    @Bean
    internal fun taskCompletionService(tasksRepository: TasksRepository): TaskCompletionService {
        return TaskCompletionService(tasksRepository)
    }

    @Bean
    internal fun tasksQueryService(tasksRepository: TasksRepository): TasksQueryService {
        return TasksQueryService(tasksRepository)
    }
}
