package dev.bettercode.tasks

data class TaskRequest(val name: String) {
    fun toTaskDto(): TaskDto {
        return TaskDto(name = name)
    }
}
