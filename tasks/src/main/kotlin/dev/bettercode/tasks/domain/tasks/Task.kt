package dev.bettercode.tasks.domain.tasks

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.domain.projects.Project
import dev.bettercode.tasks.shared.DomainResult
import java.time.ZonedDateTime

internal class Task(val name: String, val id: TaskId = TaskId(), var projectId: ProjectId? = null) {
    internal var completionDate: ZonedDateTime? = null
    private var status = TaskStatus.NEW

    override fun toString(): String {
        return "Task(name='$name', id=$id)"
    }

    fun complete() {
        status = TaskStatus.COMPLETED
        completionDate = ZonedDateTime.now()
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

    fun reopen(): DomainResult {
        if (status == TaskStatus.COMPLETED) {
            if (completionDate?.toLocalDate()?.equals(ZonedDateTime.now().toLocalDate()) == false) {
                return DomainResult.failure("Task can be reopened within the same day as completed")
            }

            status = TaskStatus.NEW
            completionDate = null
            return DomainResult.success()
        }

        return DomainResult.success()
    }
}