package dev.bettercode.dynamicprojects

import dev.bettercode.dynamicprojects.query.DynamicProjectEntity
import dev.bettercode.tasks.TaskId

data class DynamicProjectDto(
    val id: DynamicProjectId,
    val name: String,
    val tasks: Set<TaskId> = emptySet()
) {
    companion object {
        fun from(entity: DynamicProjectEntity): DynamicProjectDto {
            return DynamicProjectDto(
                name = entity.name!!,
                id = DynamicProjectId(uuid = entity.id),
                tasks = entity.tasks.map { TaskId(it) }.toSet()
            )
        }
    }
}