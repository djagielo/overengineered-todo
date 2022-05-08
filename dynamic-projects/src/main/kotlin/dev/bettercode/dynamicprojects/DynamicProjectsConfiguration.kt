package dev.bettercode.dynamicprojects

import dev.bettercode.dynamicprojects.application.DefaultDynamicProjectsService
import dev.bettercode.dynamicprojects.application.DynamicProjectHandlers
import dev.bettercode.dynamicprojects.application.ProjectRecalculationService
import dev.bettercode.dynamicprojects.application.TasksQueryService
import dev.bettercode.dynamicprojects.domain.DynamicProjectRepository
import dev.bettercode.dynamicprojects.infra.adapter.db.inmemory.DynamicProjectEntityMapper
import dev.bettercode.dynamicprojects.infra.adapter.db.inmemory.InMemoryDynamicProjectQueryRepository
import dev.bettercode.dynamicprojects.infra.adapter.db.jpa.DynamicProjectJpaRepository
import dev.bettercode.dynamicprojects.infra.adapter.db.jpa.DynamicProjectStorageService
import dev.bettercode.dynamicprojects.infra.adapter.events.DynamicProjectsSpringEventListener
import dev.bettercode.dynamicprojects.infra.adapter.tasks.TasksFacadeQueryServiceAdapter
import dev.bettercode.dynamicprojects.infra.adapter.tasks.TasksRestAdapter
import dev.bettercode.dynamicprojects.query.db.DynamicProjectEntity
import dev.bettercode.dynamicprojects.query.db.DynamicProjectQueryRepository
import dev.bettercode.dynamicprojects.query.db.InMemoryDynamicProjectRepository
import dev.bettercode.dynamicprojects.query.service.DynamicProjectQueryService
import dev.bettercode.tasks.TasksFacade
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.client.RestTemplate


@Configuration
@EnableJpaRepositories(basePackages = ["dev.bettercode.dynamicprojects.infra.adapter", "dev.bettercode.dynamicprojects.query"])
@EntityScan(basePackageClasses = [DynamicProjectEntity::class])
@EnableAsync
class DynamicProjectsConfiguration {

    companion object {
        private val inMemoryDynamicProjectRepository = InMemoryDynamicProjectRepository()
        private val inMemoryQueryRepository = InMemoryDynamicProjectQueryRepository(inMemoryDynamicProjectRepository)


        internal fun dynamicProjectsFacade(): DynamicProjectsFacade {
            return DynamicProjectsFacade(DynamicProjectQueryService(inMemoryQueryRepository))
        }

        internal fun dynamicProjectHandlers(tasksFacade: TasksFacade): DynamicProjectHandlers {
            return DynamicProjectHandlers(
                DefaultDynamicProjectsService(inMemoryDynamicProjectRepository),
                ProjectRecalculationService(
                    inMemoryDynamicProjectRepository,
                    TasksFacadeQueryServiceAdapter(tasksFacade)
                ),
                DynamicProjectQueryService(inMemoryQueryRepository)
            )
        }
    }

    @Bean
    internal fun projectCreatedListener(projectCreatedHandler: DynamicProjectHandlers): DynamicProjectsSpringEventListener {
        return DynamicProjectsSpringEventListener(projectCreatedHandler)
    }

    @Bean
    internal fun projectCreatedEventHandler(
        dynamicProjectsService: DefaultDynamicProjectsService,
        projectRecalculationService: ProjectRecalculationService,
        dynamicProjectsQueryService: DynamicProjectQueryService
    ): DynamicProjectHandlers {
        return DynamicProjectHandlers(dynamicProjectsService, projectRecalculationService, dynamicProjectsQueryService)
    }

    @Bean
    internal fun defaultDynamicProjectsService(dynamicProjectRepository: DynamicProjectRepository): DefaultDynamicProjectsService {
        return DefaultDynamicProjectsService(dynamicProjectRepository)
    }

    @Bean
    internal fun dynamicProjectStorageService(projectRepo: DynamicProjectJpaRepository): DynamicProjectRepository {
        return DynamicProjectStorageService(DynamicProjectEntityMapper(), projectRepo)
    }

    @Bean
    internal fun projectRecalculationService(
        dynamicProjectRepository: DynamicProjectRepository,
        tasksQueryService: TasksQueryService
    ): ProjectRecalculationService {
        return ProjectRecalculationService(dynamicProjectRepository, tasksQueryService)
    }

//    @Bean
//    fun tasksQueryService(tasksFacade: TasksFacade): TasksQueryService {
//        return TasksFacadeQueryServiceAdapter(tasksFacade)
//    }

    @Bean
    internal fun restTemplate(): RestTemplate? {
        return RestTemplate()
    }

    @Bean
    internal fun tasksQueryService(
        @Value("\${taskService.url}") taskServiceUrl: String,
        restTemplate: RestTemplate,
        tasksFacade: TasksFacade
    ): TasksQueryService {
        return TasksFacadeQueryServiceAdapter(tasksFacade)
        //return TasksRestAdapter(taskServiceUrl, restTemplate)
    }

    @Bean
    internal fun dynamicProjectsFacade(dynamicProjectsQueryService: DynamicProjectQueryService): DynamicProjectsFacade {
        return DynamicProjectsFacade(dynamicProjectsQueryService)
    }

    @Bean
    internal fun dynamicProjectQueryService(dynamicProjectQueryRepository: DynamicProjectQueryRepository): DynamicProjectQueryService {
        return DynamicProjectQueryService(dynamicProjectQueryRepository)
    }

    @Bean("eventsExecutor")
    internal fun threadPoolTaskExecutor(): TaskExecutor? {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 16
        executor.maxPoolSize = 32
        executor.initialize()
        return executor
    }
}