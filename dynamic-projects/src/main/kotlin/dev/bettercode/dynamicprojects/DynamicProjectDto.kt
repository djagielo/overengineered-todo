package dev.bettercode.dynamicprojects

import dev.bettercode.dynamicprojects.query.db.DynamicProjectEntity
import dev.bettercode.tasks.TaskId

data class DynamicProjectDto(
    val id: DynamicProjectId,
    val name: String,
    val tasks: Set<TaskId> = emptySet()
) {
    companion object {
        internal fun from(entity: DynamicProjectEntity): DynamicProjectDto {
            return DynamicProjectDto(
                name = entity.name!!,
                id = DynamicProjectId(uuid = entity.id),
                tasks = entity.tasks.map { TaskId(it) }.toSet()
            )
        }
    }
}