package dev.bettercode.tasks

import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.domain.tasks.TasksRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.time.LocalDate

internal class TasksQueryService(val tasksRepository: TasksRepository) {
    fun findAll(pageRequest: PageRequest): Page<Task> {
        TODO()
    }

    fun findAllCompleted(): Page<Task> {
       TODO()
    }

    fun findCompletedForDate(date: LocalDate): Page<Task> {
        TODO()
    }

    fun findNonCompletedForDate(date: LocalDate): Page<Task> {
        TODO()
    }

    fun findAllForDate(date: LocalDate): Page<Task> {
        TODO()
    }

    fun findAllForProject(projectId: ProjectId): List<Task> {
        return this.tasksRepository.findAllForProjectId(projectId)
    }
}