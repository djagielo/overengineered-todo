package dev.bettercode.tasks.application.projects

import dev.bettercode.tasks.ProjectId
import dev.bettercode.commons.events.DomainEvent

data class ProjectCreated(val projectId: ProjectId): DomainEvent()
