package dev.bettercode.tasks

import java.time.Instant

data class TaskDto(
    val id: TaskId,
    val name: String,
    val completionDate: Instant?
) {

    constructor(name: String) : this(TaskId(), name, null)

    companion object {
        internal fun from(task: Task?): TaskDto? {
            return task?.let { t -> TaskDto(t.id, t.name, t.completionDate?.toInstant()) }
        }
    }
}
