package dev.bettercode.tasks.infra.adapter.rest

import dev.bettercode.tasks.TaskDto

data class TaskRequest(val name: String) {
    fun toTaskDto(): TaskDto {
        return TaskDto(name = name)
    }
}
