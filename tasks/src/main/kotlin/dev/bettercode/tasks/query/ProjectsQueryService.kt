package dev.bettercode.tasks.query

import dev.bettercode.tasks.ProjectDto
import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.infra.adapter.db.ProjectsQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

internal class ProjectsQueryService(private val projectsQueryRepository: ProjectsQueryRepository) {
    fun getAll(pageable: Pageable = PageRequest.of(0, 100)): Page<ProjectDto> {
        return projectsQueryRepository.findAll(pageable).map {
            ProjectDto.from(it)
        }
    }

    fun findById(projectId: ProjectId): ProjectDto? {
        return projectsQueryRepository.findById(projectId.uuid).let {
            ProjectDto.from(it)
        }
    }
}