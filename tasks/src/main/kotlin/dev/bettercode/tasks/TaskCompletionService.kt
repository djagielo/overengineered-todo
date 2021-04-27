package dev.bettercode.tasks

internal class TaskCompletionService(private val tasksRepository: TasksRepository) {
    fun complete(id: TaskId): Task? {
        val task = tasksRepository.get(id)

        task?.complete()

        return task
    }

    fun uncomplete(id: TaskId): Task? {
        val task = tasksRepository.get(id)

        task?.uncomplete()

        return task
    }
}
