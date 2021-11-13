package dev.bettercode.tasks

import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.infra.adapter.db.TaskEntity
import java.time.Instant

data class TaskDto(
    val id: TaskId,
    val name: String,
    val completionDate: Instant?
) {

    constructor(name: String) : this(TaskId(), name, null)

    companion object {
        internal fun from(task: Task?): TaskDto? {
            return task?.let { t -> TaskDto(t.id, t.name, t.completionDate) }
        }

        fun from(task: TaskEntity?): TaskDto? {
            return task?.let { t -> TaskDto(TaskId(t.id), t.name!!, t.completionDate) }
        }
    }
}
