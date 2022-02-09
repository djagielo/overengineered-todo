package dev.bettercode.dynamicprojects.query.service

import dev.bettercode.dynamicprojects.DynamicProjectDto
import dev.bettercode.dynamicprojects.DynamicProjectId
import dev.bettercode.dynamicprojects.query.db.DynamicProjectQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

internal class DynamicProjectQueryService(private val dynamicProjectQueryRepository: DynamicProjectQueryRepository) {
    fun getAll(pageable: Pageable = PageRequest.of(0, 100)): Page<DynamicProjectDto> {
        return dynamicProjectQueryRepository.findAll(pageable).map {
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