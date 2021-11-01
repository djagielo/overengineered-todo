package dev.bettercode.tasks.query

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskDto
import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.domain.projects.Project
import dev.bettercode.tasks.domain.tasks.TasksRepository
import dev.bettercode.tasks.infra.adapter.db.TasksQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

internal class TasksQueryService(
    private val tasksQueryRepository: TasksQueryRepository,
    private val tasksRepository: TasksRepository
) {

    fun findById(taskId: TaskId): TaskDto? {
        return tasksRepository.get(taskId).let {
            TaskDto.from(it)
        }
    }

    fun findAllOpen(pageRequest: PageRequest, project: Project): Page<TaskDto> {
        return tasksQueryRepository.findAllOpenForProject(pageRequest, project.id)
    }

    fun findAllCompleted(pageRequest: PageRequest): Page<TaskDto> {
        return tasksQueryRepository.findAllCompleted(pageRequest)
    }

    fun findAllForProject(pageRequest: PageRequest, projectId: ProjectId): Page<TaskDto> {
        return this.tasksQueryRepository.findAllByProjectId(pageRequest, projectId)
    }
}