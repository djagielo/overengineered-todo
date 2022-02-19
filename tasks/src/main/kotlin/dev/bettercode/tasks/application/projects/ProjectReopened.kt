package dev.bettercode.tasks.application.projects

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.shared.DomainEvent

data class ProjectReopened(val projectId: ProjectId) : DomainEvent()