package dev.bettercode.dynamicprojects.application

import dev.bettercode.tasks.TaskDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TasksQueryService {
    fun getAllOpen(pageable: Pageable): Page<TaskDto>
}
