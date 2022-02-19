package dev.bettercode.tasks.domain.tasks

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.domain.projects.Project
import dev.bettercode.tasks.shared.DomainResult
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

internal class Task(val name: String, val id: TaskId = TaskId(), var projectId: ProjectId? = null) {
    var dueDate: LocalDate? = null
        private set
    internal var completionDate: Instant? = null
    private var status = TaskStatus.NEW

    override fun toString(): String {
        return "Task(name='$name', id=$id)"
    }

    fun complete(instant: Instant = Instant.now()): DomainResult {
        status = TaskStatus.COMPLETED
        completionDate = instant
        return DomainResult.success()
    }

    fun dueTo(newDueTo: LocalDate): DomainResult {
        if (status == TaskStatus.COMPLETED) {
            return DomainResult.failure("Cannot change dueDate of completed task")
        } else if (newDueTo.isBefore(LocalDate.now())) {
            return DomainResult.failure("Cannot set past dueDate")
        }

        this.dueDate = newDueTo
        return DomainResult.success()
    }

    fun isCompleted(): Boolean {
        return status == TaskStatus.COMPLETED
    }

    fun assignTo(project: Project): DomainResult {
        return when {
            project.completed -> {
                DomainResult.failure("Cannot assign to completed project")
            }
            this.isCompleted() -> {
                DomainResult.failure("Cannot assign completed task")
            }
            else -> {
                this.projectId = project.id
                DomainResult.success()
            }
        }
    }

    fun reopen(instant: Instant = Instant.now()): DomainResult {
        if (status == TaskStatus.COMPLETED) {
            if (!LocalDate.ofInstant(completionDate, ZoneId.of("UTC"))
                    .equals(LocalDate.ofInstant(instant, ZoneId.of("UTC")))
            ) {
                return DomainResult.failure("Task can be reopened within the same day as completed")
            }

            status = TaskStatus.NEW
            completionDate = null
            return DomainResult.success()
        }

        return DomainResult.success()
    }

    fun toSnapshot(): TaskSnapshot {
        return TaskSnapshot(
            id = id.uuid,
            name = name,
            completionDate = completionDate,
            status = status,
            projectId = projectId?.uuid,
            dueDate = dueDate
        )
    }

    companion object {
        fun fromSnapshot(taskSnapshot: TaskSnapshot): Task {
            val projectId = taskSnapshot.projectId?.let { ProjectId(it) }
            val task = Task(taskSnapshot.name, TaskId(taskSnapshot.id), projectId)
            task.completionDate = taskSnapshot.completionDate
            task.status = taskSnapshot.status
            taskSnapshot.dueDate?.let(task::dueTo)
            return task
        }
    }
}