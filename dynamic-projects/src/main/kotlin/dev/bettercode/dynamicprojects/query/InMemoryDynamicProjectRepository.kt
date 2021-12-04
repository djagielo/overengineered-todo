package dev.bettercode.dynamicprojects.query

import dev.bettercode.dynamicprojects.DynamicProjectId
import dev.bettercode.dynamicprojects.domain.DynamicProject
import dev.bettercode.dynamicprojects.domain.DynamicProjectRepository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class InMemoryDynamicProjectRepository : DynamicProjectRepository {
    private val db = ConcurrentHashMap<UUID, DynamicProject>()

    override fun getDefaultProjects(): List<DynamicProject> {
        return db.values.filter { it.default }.toList()
    }

    override fun getProjectById(id: DynamicProjectId): DynamicProject? {
        return db[id.uuid]
    }

    override fun saveProject(project: DynamicProject) {
        db[project.id.uuid] = project
    }

    fun getAll(): List<DynamicProject> {
        return ArrayList(db.values)
    }
}