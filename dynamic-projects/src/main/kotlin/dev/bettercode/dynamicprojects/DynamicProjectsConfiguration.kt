package dev.bettercode.dynamicprojects

import dev.bettercode.dynamicprojects.application.DefaultDynamicProjectsService
import dev.bettercode.dynamicprojects.application.DynamicProjectHandlers
import dev.bettercode.dynamicprojects.application.ProjectRecalculationService
import dev.bettercode.dynamicprojects.application.TasksQueryService
import dev.bettercode.dynamicprojects.domain.DynamicProjectRepository
import dev.bettercode.dynamicprojects.infra.adapter.db.jpa.DynamicProjectJpaRepository
import dev.bettercode.dynamicprojects.infra.adapter.db.jpa.DynamicProjectStorageService
import dev.bettercode.dynamicprojects.infra.adapter.db.inmemory.DynamicProjectEntityMapper
import dev.bettercode.dynamicprojects.infra.adapter.db.inmemory.InMemoryDynamicProjectQueryRepository
import dev.bettercode.dynamicprojects.infra.adapter.events.DynamicProjectsSpringEventListener
import dev.bettercode.dynamicprojects.infra.adapter.tasks.TasksFacadeQueryService
import dev.bettercode.dynamicprojects.query.db.DynamicProjectEntity
import dev.bettercode.dynamicprojects.query.db.DynamicProjectQueryRepository
import dev.bettercode.dynamicprojects.query.service.DynamicProjectQueryService
import dev.bettercode.dynamicprojects.query.db.InMemoryDynamicProjectRepository
import dev.bettercode.tasks.TasksFacade
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["dev.bettercode.dynamicprojects.infra.adapter", "dev.bettercode.dynamicprojects.query"])
@EntityScan(basePackageClasses = [DynamicProjectEntity::class])
internal class DynamicProjectsConfiguration {

    companion object {
        private val inMemoryDynamicProjectRepository = InMemoryDynamicProjectRepository()
        private val inMemoryQueryRepository = InMemoryDynamicProjectQueryRepository(inMemoryDynamicProjectRepository)


        internal fun dynamicProjectsFacade(): DynamicProjectsFacade {
            return DynamicProjectsFacade(DynamicProjectQueryService(inMemoryQueryRepository))
        }

        internal fun dynamicProjectHandlers(tasksFacade: TasksFacade): DynamicProjectHandlers {
            return DynamicProjectHandlers(
                DefaultDynamicProjectsService(inMemoryDynamicProjectRepository),
                ProjectRecalculationService(inMemoryDynamicProjectRepository, TasksFacadeQueryService(tasksFacade)),
                DynamicProjectQueryService(inMemoryQueryRepository)
            )
        }
    }

    @Bean
    fun projectCreatedListener(projectCreatedHandler: DynamicProjectHandlers): DynamicProjectsSpringEventListener {
        return DynamicProjectsSpringEventListener(projectCreatedHandler)
    }

    @Bean
    fun projectCreatedEventHandler(
        dynamicProjectsService: DefaultDynamicProjectsService,
        projectRecalculationService: ProjectRecalculationService,
        dynamicProjectsQueryService: DynamicProjectQueryService
    ): DynamicProjectHandlers {
        return DynamicProjectHandlers(dynamicProjectsService, projectRecalculationService, dynamicProjectsQueryService)
    }

    @Bean
    fun defaultDynamicProjectsService(dynamicProjectRepository: DynamicProjectRepository): DefaultDynamicProjectsService {
        return DefaultDynamicProjectsService(dynamicProjectRepository)
    }

    @Bean
    fun dynamicProjectStorageService(projectRepo: DynamicProjectJpaRepository): DynamicProjectRepository {
        return DynamicProjectStorageService(DynamicProjectEntityMapper(), projectRepo)
    }

    @Bean
    fun projectRecalculationService(
        dynamicProjectRepository: DynamicProjectRepository,
        tasksQueryService: TasksQueryService
    ): ProjectRecalculationService {
        return ProjectRecalculationService(dynamicProjectRepository, tasksQueryService)
    }

    @Bean
    fun tasksQueryService(tasksFacade: TasksFacade): TasksQueryService {
        return TasksFacadeQueryService(tasksFacade)
    }

    @Bean
    fun dynamicProjectsFacade(dynamicProjectsQueryService: DynamicProjectQueryService): DynamicProjectsFacade {
        return DynamicProjectsFacade(dynamicProjectsQueryService)
    }

    @Bean
    fun dynamicProjectQueryService(dynamicProjectQueryRepository: DynamicProjectQueryRepository): DynamicProjectQueryService {
        return DynamicProjectQueryService(dynamicProjectQueryRepository)
    }
}