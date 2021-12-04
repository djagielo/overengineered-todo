package dev.bettercode.dynamicprojects.query

import dev.bettercode.dynamicprojects.DynamicProjectDto
import dev.bettercode.dynamicprojects.DynamicProjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

class DynamicProjectQueryService(private val dynamicProjectQueryRepository: DynamicProjectQueryRepository) {
    fun getAll(pageRequest: PageRequest = PageRequest.of(0, 100)): Page<DynamicProjectDto> {
        return dynamicProjectQueryRepository.findAll(pageRequest).map {
            DynamicProjectDto.from(it)
        }
    }

    fun getById(id: DynamicProjectId): DynamicProjectDto? {
        return dynamicProjectQueryRepository.findById(id)?.let {
            DynamicProjectDto.from(it)
        }
    }

    fun getByName(name: String): DynamicProjectDto? {
        return dynamicProjectQueryRepository.findByName(name)?.let {
            DynamicProjectDto.from(it)
        }
    }
}