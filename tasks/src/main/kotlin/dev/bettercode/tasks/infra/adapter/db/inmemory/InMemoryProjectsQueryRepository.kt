package dev.bettercode.tasks.infra.adapter.db.inmemory

import dev.bettercode.tasks.ProjectDto
import dev.bettercode.tasks.query.ProjectsQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import kotlin.math.min

internal class InMemoryProjectsQueryRepository(private val db: InMemoryProjectRepository) : ProjectsQueryRepository {
    override fun findAll(pageRequest: PageRequest): Page<ProjectDto> {
        return listToPage(
            db.getAll().map {
                ProjectDto.from(it)!!
            }, pageRequest
        )
    }

    private fun <T> listToPage(list: List<T>, pageRequest: PageRequest): Page<T> {
        val start = pageRequest.offset.toInt()
        val end: Int = min(start + pageRequest.pageSize, list.size)
        return PageImpl(list.subList(start, end), pageRequest, list.size.toLong())
    }
}