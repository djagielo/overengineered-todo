package dev.bettercode.tasks.query

import dev.bettercode.tasks.ProjectDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

internal class ProjectsQueryService(private val projectsQueryRepository: ProjectsQueryRepository) {
    fun getAll(pageRequest: PageRequest = PageRequest.of(0, 100)): Page<ProjectDto> {
        return projectsQueryRepository.findAll(pageRequest)
    }
}