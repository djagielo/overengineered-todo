package dev.bettercode.dynamicprojects.infra.adapter.db.inmemory

import dev.bettercode.dynamicprojects.DynamicProjectId
import dev.bettercode.dynamicprojects.query.db.DynamicProjectEntity
import dev.bettercode.dynamicprojects.query.db.DynamicProjectQueryRepository
import dev.bettercode.dynamicprojects.query.db.InMemoryDynamicProjectRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import kotlin.math.min

internal class InMemoryDynamicProjectQueryRepository(val dynamicProjectQueryRepository: InMemoryDynamicProjectRepository) :
    DynamicProjectQueryRepository {
    private val mapper: DynamicProjectEntityMapper = DynamicProjectEntityMapper()
    override fun findAll(pageable: Pageable): Page<DynamicProjectEntity> {
        return listToPage(dynamicProjectQueryRepository.getAll(), pageable).map {
            mapper.toEntity(it)
        }
    }

    override fun findById(id: DynamicProjectId): DynamicProjectEntity? {
        return dynamicProjectQueryRepository.getProjectById(id)?.let {
            mapper.toEntity(it)
        }
    }

    override fun findByName(name: String): DynamicProjectEntity? {
        return dynamicProjectQueryRepository.getAll().find {
            it.name == name
        }?.let {
            mapper.toEntity(it)
        }
    }

    private fun <T> listToPage(list: List<T>, pageable: Pageable): Page<T> {
        val start = pageable.offset.toInt()
        val end: Int = min(start + pageable.pageSize, list.size)
        return PageImpl(list.subList(start, end), pageable, list.size.toLong())
    }
}
