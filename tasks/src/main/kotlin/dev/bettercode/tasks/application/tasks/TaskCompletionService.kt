package dev.bettercode.tasks.application.tasks

import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.domain.tasks.TasksRepository

internal class TaskCompletionService(private val tasksRepository: TasksRepository) {
    fun complete(id: TaskId): Task? {
        val task = tasksRepository.get(id)

        task?.let {
            it.complete()
            tasksRepository.save(it)
        }

        return task
    }

    fun uncomplete(id: TaskId): Task? {
        val task = tasksRepository.get(id)

        task?.let {
            it.uncomplete()
            tasksRepository.save(it)
        }

        return task
    }
}
