package dev.bettercode.tasks.application.tasks

import dev.bettercode.tasks.TaskId
import dev.bettercode.commons.events.DomainEvent

data class TaskCreated(val taskId: TaskId) : DomainEvent()