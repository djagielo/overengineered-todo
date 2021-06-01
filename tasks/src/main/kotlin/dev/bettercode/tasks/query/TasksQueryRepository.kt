package dev.bettercode.tasks.query

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.time.LocalDate

internal interface TasksQueryRepository {
    fun findAllForProject(pageRequest: PageRequest, projectId: ProjectId): Page<TaskDto>
    fun findAllOpenForProject(pageRequest: PageRequest, projectId: ProjectId): Page<TaskDto>
    fun findAllCompletedForProject(pageRequest: PageRequest, projectId: ProjectId): Page<TaskDto>
    fun findAllCompletedForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto>
    fun findAllOpenForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto>
    fun findAllForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto>
    fun findAllCompleted(pageRequest: PageRequest): Page<TaskDto>
}