package dev.bettercode.tasks.application.tasks

import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.domain.tasks.TasksRepository
import java.time.Clock
import java.time.Instant

internal class TaskCompletionService(private val tasksRepository: TasksRepository) {
    fun complete(id: TaskId, clock: Clock = Clock.systemDefaultZone()): Task? {
        val task = tasksRepository.get(id)

        task?.let {
            it.complete(Instant.now(clock))
            tasksRepository.save(it)
        }

        return task
    }

    fun reopen(id: TaskId, clock: Clock = Clock.systemDefaultZone()): Task? {
        val task = tasksRepository.get(id)

        task?.let {
            it.reopen(Instant.now(clock))
            tasksRepository.save(it)
        }

        return task
    }
}
