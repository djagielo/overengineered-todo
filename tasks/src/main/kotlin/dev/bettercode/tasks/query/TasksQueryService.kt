package dev.bettercode.tasks.query

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskDto
import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.domain.projects.Project
import dev.bettercode.tasks.domain.tasks.TasksRepository
import dev.bettercode.tasks.infra.adapter.db.TasksQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.time.LocalDate

internal class TasksQueryService(
    private val tasksQueryRepository: TasksQueryRepository,
    private val tasksRepository: TasksRepository
) {

    fun findById(taskId: TaskId): TaskDto? {
        return tasksRepository.get(taskId).let {
            TaskDto.from(it)
        }
    }

    fun findAllOpen(pageable: Pageable): Page<TaskDto> {
        return tasksQueryRepository.findAllOpen(pageable).map {
            TaskDto.from(it)
        }
    }

    fun findAllOpenForProject(pageRequest: PageRequest, project: Project): Page<TaskDto> {
        return tasksQueryRepository.findAllOpenForProject(pageRequest, project.id.uuid).map {
            TaskDto.from(it)
        }
    }

    fun findAllCompleted(pageRequest: PageRequest): Page<TaskDto> {
        return tasksQueryRepository.findAllCompleted(pageRequest).map {
            TaskDto.from(it)
        }
    }

    fun findAllForProject(pageRequest: PageRequest, projectId: ProjectId): Page<TaskDto> {
        return this.tasksQueryRepository.findAllByProjectId(pageRequest, projectId.uuid).map {
            TaskDto.from(it)
        }
    }

    fun findAllWithoutDueDate(pageable: Pageable): Page<TaskDto> {
        return this.tasksQueryRepository.findAllNoDueDate(pageable).map {
            TaskDto.from(it)
        }
    }

    fun findAllWithDueDate(pageable: Pageable, dueDate: LocalDate): Page<TaskDto> {
        return this.tasksQueryRepository.findAllWithDueDate(pageable, dueDate).map {
            TaskDto.from(it)
        }
    }
}