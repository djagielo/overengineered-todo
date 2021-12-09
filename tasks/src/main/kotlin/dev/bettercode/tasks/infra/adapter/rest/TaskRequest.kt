package dev.bettercode.tasks.infra.adapter.rest

import dev.bettercode.tasks.TaskDto
import java.time.LocalDate

data class TaskRequest(val name: String, val dueDate: LocalDate?) {
    fun toTaskDto(): TaskDto {
        return TaskDto(name = name, dueDate = dueDate)
    }
}
