package dev.bettercode.tasks.application.projects

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.domain.projects.ProjectRepository
import dev.bettercode.tasks.shared.DomainResult

internal class ProjectCompletionService(private val projectRepository: ProjectRepository) {
    fun complete(projectId: ProjectId): DomainResult {
        return projectRepository.get(projectId)?.let {
            it.complete()
            projectRepository.save(it)
            DomainResult.success()
        } ?: DomainResult.failure("No project with given id")
    }

}
