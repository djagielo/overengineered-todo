package dev.bettercode.tasks

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class TasksConfiguration {
    companion object {
        fun tasksFacade(): TasksFacade {
            val taskRepo = InMemoryTasksRepository()
            return TasksFacade(TaskCrudService(taskRepo), TaskCompletionService(taskRepo))
        }
    }

    @Bean
    internal fun taskRepository(): InMemoryTasksRepository {
        val repo = InMemoryTasksRepository()

        repo.add(Task("one"))
        repo.add(Task("two"))
        repo.add(Task("three"))

        return repo
    }

    @Bean
    internal fun tasksFacade(tasksCrudUseCase: TaskCrudService, tasksCompletionService: TaskCompletionService): TasksFacade {
        return TasksFacade(tasksCrudUseCase, tasksCompletionService)
    }

    @Bean
    internal fun tasksCrudUseCase(tasksRepository: TasksRepository): TaskCrudService {
        return TaskCrudService(tasksRepository)
    }

    @Bean
    internal fun taskCompletionService(tasksRepository: TasksRepository): TaskCompletionService {
        return TaskCompletionService(tasksRepository)
    }

}
