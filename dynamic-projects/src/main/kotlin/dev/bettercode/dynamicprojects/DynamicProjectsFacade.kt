package dev.bettercode.dynamicprojects

import dev.bettercode.dynamicprojects.query.DynamicProjectQueryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

class DynamicProjectsFacade internal constructor(
    private val dynamicProjectQueryService: DynamicProjectQueryService
) {
    fun getProjects(pageRequest: PageRequest = PageRequest.of(0, 100)): Page<DynamicProjectDto> {
        return this.dynamicProjectQueryService.getAll(pageRequest)
    }

    fun getProjectByName(name: String): DynamicProjectDto? {
        return this.dynamicProjectQueryService.getByName(name)
    }

    fun getProjectById(id: DynamicProjectId): DynamicProjectDto? {
        return this.dynamicProjectQueryService.getById(id)
    }
}