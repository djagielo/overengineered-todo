package dev.bettercode.dynamicprojects.domain

import dev.bettercode.dynamicprojects.DynamicProjectId
import dev.bettercode.tasks.TaskId

class DynamicProject(
    val id: DynamicProjectId = DynamicProjectId(),
    val tasks: MutableSet<TaskId> =  mutableSetOf(),
    val name: String,
    val default: Boolean = false
) {
    fun addTasks(taskIds: Collection<TaskId>) {
        this.tasks.addAll(taskIds)
    }

    fun tasks(): Set<TaskId> {
        return this.tasks
    }

    fun clearTasks() {
        this.tasks.clear()
    }
}