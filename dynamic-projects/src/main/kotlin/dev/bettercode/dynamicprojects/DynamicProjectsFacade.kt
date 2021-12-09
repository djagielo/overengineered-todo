package dev.bettercode.dynamicprojects

import dev.bettercode.dynamicprojects.query.service.DynamicProjectQueryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

class DynamicProjectsFacade internal constructor(
    private val dynamicProjectQueryService: DynamicProjectQueryService
) {
    fun getProjects(pageable: Pageable = PageRequest.of(0, 100)): Page<DynamicProjectDto> {
        return this.dynamicProjectQueryService.getAll(pageable)
    }

    fun getProjectByName(name: String): DynamicProjectDto? {
        return this.dynamicProjectQueryService.getByName(name)
    }

    fun getProjectById(id: DynamicProjectId): DynamicProjectDto? {
        return this.dynamicProjectQueryService.getById(id)
    }
}