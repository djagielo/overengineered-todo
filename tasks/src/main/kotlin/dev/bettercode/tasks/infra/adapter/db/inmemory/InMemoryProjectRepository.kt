package dev.bettercode.tasks.infra.adapter.db.inmemory

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.domain.projects.Inbox
import dev.bettercode.tasks.domain.projects.Project
import dev.bettercode.tasks.domain.projects.ProjectRepository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

internal class InMemoryProjectRepository : ProjectRepository {
    private val db = ConcurrentHashMap<UUID, Project>()
    override fun add(project: Project): Project {
        db[project.id.uuid] = project
        return project
    }

    override fun get(projectId: ProjectId): Project? {
        return db[projectId.uuid]
    }

    override fun getInboxProject(): Project? {
        return db.values.find {
            it.name == Inbox.NAME
        }
    }

    override fun save(project: Project) {
        db[project.id.uuid] = project
    }

    override fun delete(projectId: ProjectId) {
        db.remove(projectId.uuid)
    }

    fun getAll(): List<Project> {
        return ArrayList(db.values)
    }

    override fun createInbox(): Project {
        Project(name = "INBOX").let {
            db[it.id.uuid] = it
            return it
        }
    }
}