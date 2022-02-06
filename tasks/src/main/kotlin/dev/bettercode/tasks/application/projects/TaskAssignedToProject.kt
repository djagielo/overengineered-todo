package dev.bettercode.tasks.application.projects

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.shared.DomainEvent

data class TaskAssignedToProject(val taskId: TaskId, val projectId: ProjectId) : DomainEvent()