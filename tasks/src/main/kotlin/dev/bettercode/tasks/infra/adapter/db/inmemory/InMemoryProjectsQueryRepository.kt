package dev.bettercode.tasks.infra.adapter.db.inmemory

import dev.bettercode.tasks.infra.adapter.db.ProjectEntity
import dev.bettercode.tasks.infra.adapter.db.ProjectEntityMapper
import dev.bettercode.tasks.infra.adapter.db.ProjectsQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*
import kotlin.math.min

internal class InMemoryProjectsQueryRepository(
    private val db: InMemoryProjectRepository,
    private val mapper: ProjectEntityMapper = ProjectEntityMapper()
) : ProjectsQueryRepository {
    override fun findAll(pageable: Pageable): Page<ProjectEntity> {
        return listToPage(
            db.getAll().map { mapper.toEntity(it) }, pageable
        )
    }

    private fun <T> listToPage(list: List<T>, pageable: Pageable): Page<T> {
        val start = pageable.offset.toInt()
        val end: Int = min(start + pageable.pageSize, list.size)
        return PageImpl(list.subList(start, end), pageable, list.size.toLong())
    }

    override fun findById(uuid: UUID): ProjectEntity? {
        return db.getAll().find {
            it.id.uuid == uuid
        }?.let {
            mapper.toEntity(it)
        }
    }
}