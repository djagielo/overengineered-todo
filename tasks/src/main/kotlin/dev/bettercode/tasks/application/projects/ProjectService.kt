package dev.bettercode.tasks.application.projects

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.domain.projects.Project
import dev.bettercode.tasks.domain.projects.ProjectRepository
import dev.bettercode.tasks.shared.DomainEventPublisher

internal class ProjectService(
    private val projectRepository: ProjectRepository,
    private val eventPublisher: DomainEventPublisher
) {
    fun add(project: Project): Project {
        val result = projectRepository.add(project)
        eventPublisher.publish(ProjectCreated(result.id))
        return result
    }

    fun delete(projectId: ProjectId) {
        projectRepository.delete(projectId)
    }

    fun getInboxProject(): Project {
        var inbox = projectRepository.getInboxProject()
        if (inbox == null) {
            inbox = projectRepository.createInbox()
            eventPublisher.publish(ProjectCreated(inbox.id))
        }
        return inbox
    }
}
