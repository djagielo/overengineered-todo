package dev.bettercode.audit.service

import dev.bettercode.audit.repository.AuditLog
import dev.bettercode.tasks.application.projects.ProjectCompleted
import dev.bettercode.tasks.application.projects.ProjectCreated
import dev.bettercode.tasks.application.projects.ProjectReopened
import dev.bettercode.tasks.application.tasks.TaskCompleted
import dev.bettercode.tasks.application.tasks.TaskCreated
import dev.bettercode.tasks.application.tasks.TaskReopened

internal class AuditLogFactory{
    companion object {
        fun from (event: TaskCreated): AuditLog {
            return AuditLog(msg = "Task with id=${event.taskId} has been created")
        }

        fun from (event: ProjectCreated): AuditLog {
            return AuditLog(msg = "Project with id=${event.projectId} has been created")
        }

        fun from(event: ProjectCompleted): AuditLog {
            return AuditLog(msg = "Project with id=${event.projectId} has been completed")
        }

        fun from(event: ProjectReopened): AuditLog {
            return AuditLog(msg = "Project with id=${event.projectId} has been reopened")
        }

        fun from(event: TaskCompleted): AuditLog {
            return AuditLog(msg = "Task with id=${event.taskId} has been completed")
        }

        fun from(event: TaskReopened): AuditLog {
            return AuditLog(msg = "Task with id=${event.taskId} has been reopened")
        }
    }
}
