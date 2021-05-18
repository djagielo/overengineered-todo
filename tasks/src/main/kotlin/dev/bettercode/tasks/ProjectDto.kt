package dev.bettercode.tasks

import dev.bettercode.tasks.domain.projects.Project

data class ProjectDto(
    val id: ProjectId,
    val name: String,
) {
    constructor(name: String) : this(ProjectId(), name)

    companion object {
        internal fun from(project: Project?): ProjectDto? {
            return project?.let { p -> ProjectDto(p.id, p.name) }
        }
    }
}
