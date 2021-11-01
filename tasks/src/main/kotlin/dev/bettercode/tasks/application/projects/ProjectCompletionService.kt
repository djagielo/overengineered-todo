package dev.bettercode.tasks.application.projects

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.domain.projects.ProjectRepository
import dev.bettercode.tasks.shared.DomainResult

internal class ProjectCompletionService(private val projectRepository: ProjectRepository) {
    fun complete(projectId: ProjectId): DomainResult {
        return projectRepository.get(projectId)?.let {
            val result = it.complete()
            if (result.successful) {
                projectRepository.save(it)
            }
            return result
        } ?: DomainResult.failure("No project with given id")
    }

    fun reopen(projectId: ProjectId): DomainResult {
        return projectRepository.get(projectId)?.let {
            it.reopen()
            projectRepository.save(it)
            DomainResult.success()
        } ?: DomainResult.failure("No project with given id")
    }
}
