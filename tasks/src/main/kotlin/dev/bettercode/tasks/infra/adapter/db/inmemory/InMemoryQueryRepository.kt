@file:Suppress("unused")

package dev.bettercode.tasks.infra.adapter.db.inmemory

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.infra.adapter.db.TaskEntity
import dev.bettercode.tasks.infra.adapter.db.TaskEntityMapper
import dev.bettercode.tasks.infra.adapter.db.TasksQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDate
import java.util.*
import kotlin.math.min


internal class InMemoryQueryRepository(private val inMemoryTasksDb: InMemoryTasksRepository) :
    TasksQueryRepository {
    private val mapper: TaskEntityMapper = TaskEntityMapper()
    private fun getAllForProject(projectId: ProjectId): List<Task> {
        return inMemoryTasksDb.getAll().filter {
            it.projectId?.equals(projectId) == true
        }
    }

    override fun findAllByProjectId(pageable: Pageable, uuid: UUID): Page<TaskEntity> {
        return listToPage(getAllForProject(ProjectId(uuid)).map { mapper.toEntity(it) }, pageable)
    }

    override fun findAllOpenForProject(pageable: Pageable, uuid: UUID): Page<TaskEntity> {
        return listToPage(
            getAllForProject(ProjectId(uuid)).filter {
                !it.isCompleted()
            }.map { mapper.toEntity(it) },
            pageable
        )
    }

    override fun findAllCompleted(pageable: Pageable): Page<TaskEntity> {
        return findWithFilter(pageable) { it.isCompleted() }
    }

    private fun findWithFilter(pageable: Pageable, predicate: (Task) -> Boolean): Page<TaskEntity> {
        return listToPage(
            inMemoryTasksDb.getAll()
                .filter(predicate)
                .map { mapper.toEntity(it) }, pageable
        )
    }

    override fun findAllOpen(pageable: Pageable): Page<TaskEntity> {
        return findWithFilter(pageable) { !it.isCompleted() }
    }

    override fun findAllNoDueDate(pageable: Pageable): Page<TaskEntity> {
        return findWithFilter(pageable) { it.dueDate == null }
    }

    override fun findAllWithDueDate(pageable: Pageable, dueDate: LocalDate): Page<TaskEntity> {
        return findWithFilter(pageable) { it.dueDate?.equals(dueDate) ?: false }
    }

    private fun <T> listToPage(list: List<T>, pageable: Pageable): Page<T> {
        val start = pageable.offset.toInt()
        val end: Int = min(start + pageable.pageSize, list.size)
        return PageImpl(list.subList(start, end), pageable, list.size.toLong())
    }
}