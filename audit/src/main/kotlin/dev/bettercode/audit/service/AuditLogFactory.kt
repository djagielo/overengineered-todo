package dev.bettercode.audit.service

import dev.bettercode.audit.repository.AuditLog
import dev.bettercode.tasks.application.projects.ProjectCreated
import dev.bettercode.tasks.application.tasks.TaskCreated

internal class AuditLogFactory{
    companion object {
        fun from (event: TaskCreated): AuditLog {
            return AuditLog(msg = "Task with id=${event.taskId} has been created ")
        }

        fun from (event: ProjectCreated): AuditLog {
            return AuditLog(msg = "Project with id=${event.projectId} has been created ")
        }
    }
}
