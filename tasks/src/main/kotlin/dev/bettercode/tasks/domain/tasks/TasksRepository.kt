package dev.bettercode.tasks.domain.tasks

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskId

internal interface TasksRepository {
    fun add(task: Task): Task
    fun get(id: TaskId): Task?
    fun save(task: Task): Task
    fun getAll(): List<Task>
    fun delete(id: TaskId)
    fun getAllCompleted(): List<Task>
    fun findAllForProjectId(projectId: ProjectId): List<Task>
}
