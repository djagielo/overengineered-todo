package dev.bettercode.tasks.application.projects;

import dev.bettercode.tasks.domain.projects.Project
import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.domain.projects.Inbox
import dev.bettercode.tasks.domain.projects.ProjectRepository
import dev.bettercode.tasks.domain.tasks.Task
import dev.bettercode.tasks.domain.tasks.TasksRepository

internal class ProjectCrudService(
    private val projectRepository: ProjectRepository
) {
    fun add(project: Project): Project {
        return projectRepository.add(project)
    }

    fun delete(projectId: ProjectId) {
        projectRepository.delete(projectId)
    }

    fun getAll(): List<Project> {
        return projectRepository.getAll()
    }

    fun get(projectId: ProjectId): Project? {
        return projectRepository.get(projectId)
    }

    fun getInboxProject(): Project {
        val inbox = projectRepository.getInboxProject()
        return inbox ?: projectRepository.add(Inbox())
    }
}
