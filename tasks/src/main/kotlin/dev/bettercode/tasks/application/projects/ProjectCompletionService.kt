package dev.bettercode.tasks.application.projects

import dev.bettercode.commons.events.AuditLogCommand
import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.domain.projects.ProjectRepository
import dev.bettercode.tasks.shared.DomainEventPublisher
import dev.bettercode.tasks.shared.DomainResult

internal class ProjectCompletionService(
    private val projectRepository: ProjectRepository,
    private val eventPublisher: DomainEventPublisher
) {
    fun complete(projectId: ProjectId): DomainResult {
        return projectRepository.get(projectId)?.let {
            val result = it.complete()
            if (result.successful) {
                projectRepository.save(it)
                eventPublisher.publish(ProjectCompleted(projectId))
                eventPublisher.publish(AuditLogCommand(message = "Project with id=ProjectId(uuid=${projectId.uuid}) has been completed"))
            }
            return result
        } ?: DomainResult.failure("No project with given id")
    }

    fun reopen(projectId: ProjectId): DomainResult {
        return projectRepository.get(projectId)?.let {
            it.reopen()
            projectRepository.save(it)
            eventPublisher.publish(ProjectReopened(projectId))
            eventPublisher.publish(AuditLogCommand(message = "Project with id=ProjectId(uuid=${projectId.uuid}) has been reopened"))
            DomainResult.success()
        } ?: DomainResult.failure("No project with given id")
    }
}
