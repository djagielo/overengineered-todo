@file:Suppress("unused")

package dev.bettercode.tasks

import dev.bettercode.tasks.application.projects.ProjectAssignmentService
import dev.bettercode.tasks.application.projects.ProjectCompletionService
import dev.bettercode.tasks.application.projects.ProjectService
import dev.bettercode.tasks.application.tasks.TaskCompletionService
import dev.bettercode.tasks.application.tasks.TaskService
import dev.bettercode.tasks.domain.projects.ProjectRepository
import dev.bettercode.tasks.domain.tasks.TasksRepository
import dev.bettercode.tasks.infra.adapter.db.*
import dev.bettercode.tasks.infra.adapter.db.inmemory.InMemoryProjectRepository
import dev.bettercode.tasks.infra.adapter.db.inmemory.InMemoryProjectsQueryRepository
import dev.bettercode.tasks.infra.adapter.db.inmemory.InMemoryQueryRepository
import dev.bettercode.tasks.infra.adapter.db.inmemory.InMemoryTasksRepository
import dev.bettercode.tasks.query.ProjectsQueryService
import dev.bettercode.tasks.query.TasksQueryService
import dev.bettercode.tasks.shared.DomainEventPublisher
import dev.bettercode.tasks.shared.InMemoryEventPublisher
import dev.bettercode.tasks.shared.SpringEventPublisher
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
@EnableJpaRepositories("dev.bettercode.tasks.infra.adapter.db")
@EntityScan(basePackageClasses = [TaskEntity::class])
@Import(TaskEntity::class)
class TasksConfiguration {
    companion object {
        fun tasksFacade(inMemoryEventPublisher: InMemoryEventPublisher = InMemoryEventPublisher()): TasksFacade {
            val taskRepo = InMemoryTasksRepository()
            val projectRepo = InMemoryProjectRepository()
            val tasksQueryRepository = InMemoryQueryRepository(taskRepo)
            val projectsQueryRepository = InMemoryProjectsQueryRepository(projectRepo)
            val projectService = ProjectService(projectRepo, inMemoryEventPublisher)
            val projectsQueryService = ProjectsQueryService(projectsQueryRepository)
            return TasksFacade(
                TaskService(taskRepo, projectRepo, projectService, inMemoryEventPublisher),
                TaskCompletionService(taskRepo, inMemoryEventPublisher),
                projectService,
                ProjectAssignmentService(projectRepo, taskRepo, inMemoryEventPublisher),
                ProjectCompletionService(projectRepo, inMemoryEventPublisher),
                TasksQueryService(tasksQueryRepository, taskRepo),
                projectsQueryService
            )
        }
    }

    @Bean
    internal fun taskRepository(jdbcTemplate: JdbcTemplate): TasksRepository {
        return JdbcTasksRepository(jdbcTemplate)
    }

    @Bean
    internal fun projectsRepository(jdbcTemplate: JdbcTemplate): ProjectRepository {
        return JdbcProjectRepository(jdbcTemplate)
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
        projectRepository: ProjectRepository,
        projectService: ProjectService,
        eventPublisher: DomainEventPublisher
    ): TaskService {
        return TaskService(tasksRepository, projectRepository, projectService, eventPublisher)
    }

    @Bean
    internal fun projectsCrudService(
        tasksRepository: TasksRepository,
        projectRepository: ProjectRepository,
        eventPublisher: DomainEventPublisher
    ): ProjectService {
        return ProjectService(projectRepository, eventPublisher)
    }

    @Bean
    internal fun projectsAssignmentService(
        projectRepository: ProjectRepository,
        tasksRepository: TasksRepository
    ): ProjectAssignmentService {
        return ProjectAssignmentService(projectRepository, tasksRepository, InMemoryEventPublisher())
    }

    @Bean
    internal fun taskCompletionService(
        tasksRepository: TasksRepository,
        eventPublisher: DomainEventPublisher
    ): TaskCompletionService {
        return TaskCompletionService(tasksRepository, eventPublisher)
    }

    @Bean
    internal fun tasksQueryService(
        tasksQueryRepository: TasksQueryRepository,
        tasksRepository: TasksRepository
    ): TasksQueryService {
        return TasksQueryService(tasksQueryRepository, tasksRepository)
    }

    @Bean
    internal fun projectCompletionService(
        projectRepository: ProjectRepository,
        eventPublisher: DomainEventPublisher
    ): ProjectCompletionService {
        return ProjectCompletionService(projectRepository, eventPublisher)
    }

    @Bean
    internal fun projectsQueryService(projectsQueryRepository: ProjectsQueryRepository): ProjectsQueryService {
        return ProjectsQueryService(projectsQueryRepository)
    }

    @Bean
    internal fun domainEventPublisher(eventPublisher: ApplicationEventPublisher): DomainEventPublisher {
        return SpringEventPublisher(eventPublisher)
    }
}
