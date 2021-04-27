package dev.bettercode.tasks

internal interface TasksRepository {
    fun add(task: Task): Task
    fun get(id: TaskId): Task?
    fun getAll(): List<Task>
    fun delete(id: TaskId)
    fun getAllCompleted(): List<Task>
}
