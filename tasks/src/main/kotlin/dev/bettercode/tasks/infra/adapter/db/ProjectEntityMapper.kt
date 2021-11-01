package dev.bettercode.tasks.infra.adapter.db

import dev.bettercode.tasks.domain.projects.Project

internal class ProjectEntityMapper {
    fun toEntity(project: Project): ProjectEntity {
        return ProjectEntity(id = project.id.uuid, name = project.name)
    }
}