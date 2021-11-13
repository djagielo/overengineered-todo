@file:Suppress("unused")

package dev.bettercode.tasks.infra.adapter.db.inmemory

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskDto
import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.infra.adapter.db.TaskEntity
import dev.bettercode.tasks.infra.adapter.db.TaskEntityMapper
import dev.bettercode.tasks.infra.adapter.db.TasksQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
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

    override fun findAllOpenForProject(pageRequest: Pageable, uuid: UUID): Page<TaskEntity> {
        return listToPage(
            getAllForProject(ProjectId(uuid)).filter {
                !it.isCompleted()
            }.map { mapper.toEntity(it) },
            pageRequest
        )
    }

    //
//    override fun findAllCompletedForProject(pageRequest: PageRequest, projectId: ProjectId): Page<TaskDto> {
//        return listToPage(
//            getAllForProject(projectId).filter {
//                it.isCompleted()
//            }.map { TaskDto.from(it)!! },
//            pageRequest
//        )
//    }
//
//    override fun findAllCompletedForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto> {
//        return listToPage(
//            inMemoryTasksDb.getAll().filter {
//                LocalDate.ofInstant(it.completionDate, ZoneId.of("UTC")).equals(date)
//            }.map { TaskDto.from(it)!! },
//            pageRequest
//        )
//    }
//
//    override fun findAllOpenForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto> {
//        return listToPage(
//            inMemoryTasksDb.getAll().filter {
//                LocalDate.ofInstant(it.completionDate, ZoneId.of("UTC")).equals(date)
//            }.filter {
//                !it.isCompleted()
//            }.map { TaskDto.from(it)!! },
//            pageRequest
//        )
//    }
//
//    override fun findAllForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto> {
//        return listToPage(
//            inMemoryTasksDb.getAll().filter {
//                LocalDate.ofInstant(it.completionDate, ZoneId.of("UTC")).equals(date)
//            }.filter {
//                !it.isCompleted()
//            }.map { TaskDto.from(it)!! },
//            pageRequest
//        )
//    }
//
    override fun findAllCompleted(pageRequest: Pageable): Page<TaskEntity> {
        return listToPage(
            inMemoryTasksDb.getAll()
                .filter { it.isCompleted() }
                .map { mapper.toEntity(it) }, pageRequest
        )
    }

    private fun <T> listToPage(list: List<T>, pageable: Pageable): Page<T> {
        val start = pageable.offset.toInt()
        val end: Int = min(start + pageable.pageSize, list.size)
        return PageImpl(list.subList(start, end), pageable, list.size.toLong())
    }
}