package dev.bettercode.tasks.query

import dev.bettercode.tasks.ProjectDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

internal interface ProjectsQueryRepository {
    fun findAll(pageRequest: PageRequest): Page<ProjectDto>
}
