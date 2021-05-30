package dev.bettercode.tasks.query

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskDto
import dev.bettercode.tasks.domain.projects.Project
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.time.LocalDate

internal class TasksQueryService(private val tasksQueryRepository: TasksQueryRepository) {

    fun findAll(pageRequest: PageRequest, project: Project): Page<TaskDto> {
        return tasksQueryRepository.findAllForProject(pageRequest, project.id)
    }

    fun findAllOpen(pageRequest: PageRequest, project: Project): Page<TaskDto> {
        return tasksQueryRepository.findAllOpenForProject(pageRequest, project.id)
    }

    fun findAllCompleted(pageRequest: PageRequest, project: Project): Page<TaskDto> {
        return tasksQueryRepository.findAllCompletedForProject(pageRequest, project.id)
    }

    fun findCompletedForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto> {
        return tasksQueryRepository.findAllCompletedForDate(pageRequest, date)
    }

    fun findAllOpenForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto> {
        return tasksQueryRepository.findAllOpenForDate(pageRequest, date)
    }

    fun findAllForDate(pageRequest: PageRequest, date: LocalDate): Page<TaskDto> {
        return tasksQueryRepository.findAllForDate(pageRequest, date)
    }

    fun findAllForProject(pageRequest: PageRequest, projectId: ProjectId): Page<TaskDto> {
        return this.tasksQueryRepository.findAllForProject(pageRequest, projectId)
    }
}