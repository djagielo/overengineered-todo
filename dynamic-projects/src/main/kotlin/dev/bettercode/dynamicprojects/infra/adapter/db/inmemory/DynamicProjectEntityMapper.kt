package dev.bettercode.dynamicprojects.infra.adapter.db.inmemory

import dev.bettercode.dynamicprojects.DynamicProjectId
import dev.bettercode.dynamicprojects.domain.DynamicProject
import dev.bettercode.dynamicprojects.query.db.DynamicProjectEntity
import dev.bettercode.tasks.TaskId

class DynamicProjectEntityMapper {
    fun toEntity(project: DynamicProject): DynamicProjectEntity {
        return DynamicProjectEntity(
            id = project.id.uuid,
            name = project.name,
            tasks = project.tasks.map { it.uuid }.toSet(),
        )
    }

    fun fromEntity(entity: DynamicProjectEntity): DynamicProject {
        return DynamicProject(
            id = DynamicProjectId(entity.id),
            name = entity.name ?: "",
            tasks = entity.tasks.map { TaskId(it) }.toMutableSet()
        )
    }
}
