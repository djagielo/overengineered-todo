package dev.bettercode.tasks

internal class TaskCrudService (private val tasksRepository: TasksRepository) {
    fun add(task: Task): Task {
        return tasksRepository.add(task)
    }

    fun get(id: TaskId): Task? {
        return tasksRepository.get(id)
    }

    fun getAll(): List<Task> {
        return tasksRepository.getAll()
    }

    fun delete(id: TaskId) {
        tasksRepository.delete(id)
    }

    fun getAllCompleted(): List<Task> {
       return tasksRepository.getAllCompleted()
    }
}