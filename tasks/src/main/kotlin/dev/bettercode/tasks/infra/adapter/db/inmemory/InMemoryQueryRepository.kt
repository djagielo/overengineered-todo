@file:Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused")

package dev.bettercode.tasks.infra.adapter.db.inmemory

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskDto
import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.query.TasksQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.min


internal class InMemoryQueryRepository(private val inMemoryTasksDb: InMemoryTasksRepository) : TasksQueryRepository {

    private fun getAllForProject(projectId: ProjectId): List<Task> {
        return inMemoryTasksDb.getAll().filter {
            it.projectId?.equals(projectId) == true
        }
    }

    override fun findAllForProject(pageRequest: PageRequest, projectId: ProjectId): Page<TaskDto> {
        return listToPage(getAllForProject(projectId).map { TaskDto.from(it)!! }, pageRequest)
    }

    override fun findAllOpenForProject(pageRequest: PageRequest, projectId: ProjectId): Page<TaskDto> {
        return listToPage(
            getAllForProject(projectId).filter {
                !it.isCompleted()
            }.map { TaskDto.from(it)!! },
            pageRequest
        )
    }

    override fun findAllCompletedForProject(pageRequest: PageRequest, projectId: ProjectId): Page<TaskDto> {
        return listToPage(
            getAllForProject(projectId).filter {
                it.isCompleted()
            }.map { TaskDto.from(it)!! },
            pageRequest
        )
    }

    override fun findAllCompletedForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto> {
        return listToPage(
            inMemoryTasksDb.getAll().filter {
                LocalDate.ofInstant(it.completionDate, ZoneId.of("UTC")).equals(date)
            }.map { TaskDto.from(it)!! },
            pageRequest
        )
    }

    override fun findAllOpenForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto> {
        return listToPage(
            inMemoryTasksDb.getAll().filter {
                LocalDate.ofInstant(it.completionDate, ZoneId.of("UTC")).equals(date)
            }.filter {
                !it.isCompleted()
            }.map { TaskDto.from(it)!! },
            pageRequest
        )
    }

    override fun findAllForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto> {
        return listToPage(
            inMemoryTasksDb.getAll().filter {
                LocalDate.ofInstant(it.completionDate, ZoneId.of("UTC")).equals(date)
            }.filter {
                !it.isCompleted()
            }.map { TaskDto.from(it)!! },
            pageRequest
        )
    }

    override fun findAllCompleted(pageRequest: PageRequest): Page<TaskDto> {
        return listToPage(
            inMemoryTasksDb.getAllCompleted()
                .map { TaskDto.from(it)!! }, pageRequest
        )
    }

    private fun <T> listToPage(list: List<T>, pageRequest: PageRequest): Page<T> {
        val start = pageRequest.offset.toInt()
        val end: Int = min(start + pageRequest.pageSize, list.size)
        return PageImpl(list.subList(start, end), pageRequest, list.size.toLong())
    }
}