package dev.bettercode.tasks.application.projects

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.domain.projects.Inbox
import dev.bettercode.tasks.domain.projects.Project
import dev.bettercode.tasks.domain.projects.ProjectRepository

internal class ProjectService(
    private val projectRepository: ProjectRepository
) {
    fun add(project: Project): Project {
        return projectRepository.add(project)
    }

    fun delete(projectId: ProjectId) {
        projectRepository.delete(projectId)
    }

    fun getInboxProject(): Project {
        val inbox = projectRepository.getInboxProject()
        return inbox ?: projectRepository.add(Inbox())
    }
}
