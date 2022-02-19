package dev.bettercode.tasks.application.tasks

import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.shared.DomainEvent

data class TaskCompleted(val taskId: TaskId) : DomainEvent()