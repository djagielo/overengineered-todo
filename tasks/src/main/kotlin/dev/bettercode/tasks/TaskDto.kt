package dev.bettercode.tasks

import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.infra.adapter.db.TaskEntity
import java.time.Instant
import java.time.LocalDate

data class TaskDto(
    val id: TaskId,
    val name: String,
    val completionDate: Instant?,
    val dueDate: LocalDate? = null
) {

    constructor(name: String, completionDate: Instant? = null, dueDate: LocalDate? = null) : this(
        TaskId(),
        name,
        completionDate,
        dueDate
    )

    companion object {
        internal fun from(task: Task?): TaskDto? {
            return task?.let { t ->
                TaskDto(
                    id = t.id,
                    name = t.name,
                    completionDate = t.completionDate,
                    dueDate = t.dueDate
                )
            }
        }

        fun from(task: TaskEntity?): TaskDto? {
            return task?.let { t ->
                TaskDto(
                    id = TaskId(t.id),
                    name = t.name!!,
                    completionDate = t.completionDate,
                    dueDate = task.dueDate
                )
            }
        }
    }
}
