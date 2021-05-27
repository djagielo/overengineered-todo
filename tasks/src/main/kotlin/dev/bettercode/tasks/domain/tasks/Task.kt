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
        return if (project.completed) {
            DomainResult.failure("Cannot assign to completed project")
        } else {
            this.projectId = project.id
            DomainResult.success()
        }
    }

    fun uncomplete() {
        if (status == TaskStatus.COMPLETED) {
            if (completionDate?.toLocalDate()?.equals(ZonedDateTime.now().toLocalDate()) == false) {
                // not the same day, cannot be uncompleted
            }

            status = TaskStatus.NEW
            completionDate = null
        }
    }
}