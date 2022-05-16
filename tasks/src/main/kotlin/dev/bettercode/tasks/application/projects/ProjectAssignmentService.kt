package dev.bettercode.tasks.application.projects

import dev.bettercode.commons.events.AuditLogCommand
import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.TaskId
import dev.bettercode.tasks.domain.projects.ProjectRepository
import dev.bettercode.tasks.domain.tasks.TasksRepository
import dev.bettercode.tasks.shared.DomainEventPublisher
import dev.bettercode.tasks.shared.DomainResult


internal class ProjectAssignmentService(
    private val projectRepository: ProjectRepository,
    private val tasksRepository: TasksRepository,
    private val domainEventPublisher: DomainEventPublisher
) {
    fun assign(taskId: TaskId, projectId: ProjectId): DomainResult {
        return projectRepository.get(projectId)
            ?.let { project ->
                tasksRepository.get(taskId)?.let {
                    val result = it.assignTo(project)
                    return if (result.successful) {
                        tasksRepository.save(it)
                        domainEventPublisher.publish(TaskAssignedToProject(taskId, projectId))
                        domainEventPublisher.publish(AuditLogCommand(message = "Task with id=TaskId(uuid=${taskId.uuid}) has been assigned to project with id=ProjectId(uuid=${projectId.uuid})"))
                        return result
                    } else {
                        result
                    }
                } ?: DomainResult.failure("No task with given id")
            } ?: DomainResult.failure("No project with given id")
    }
}
