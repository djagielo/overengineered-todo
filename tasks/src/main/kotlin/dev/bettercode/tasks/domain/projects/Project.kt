package dev.bettercode.tasks.domain.projects

import dev.bettercode.tasks.ProjectId

open class Project(
    val name: String,
    val id: ProjectId = ProjectId(),
)
