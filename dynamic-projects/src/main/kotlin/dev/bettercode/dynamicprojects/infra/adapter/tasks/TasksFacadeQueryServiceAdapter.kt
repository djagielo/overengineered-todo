package dev.bettercode.dynamicprojects.infra.adapter.tasks

import dev.bettercode.dynamicprojects.application.TasksQueryService
import dev.bettercode.tasks.TaskDto
import dev.bettercode.tasks.TasksFacade
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class TasksFacadeQueryServiceAdapter(private val tasksFacade: TasksFacade) : TasksQueryService {
    override fun getAllOpen(pageable: Pageable): Page<TaskDto> {
        return tasksFacade.getAllOpen(pageable)
    }
}