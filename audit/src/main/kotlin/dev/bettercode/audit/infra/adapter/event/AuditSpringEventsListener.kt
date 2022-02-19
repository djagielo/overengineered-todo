package dev.bettercode.audit.infra.adapter.event

import dev.bettercode.audit.service.AuditLogFactory
import dev.bettercode.audit.service.AuditService
import dev.bettercode.tasks.application.projects.ProjectCompleted
import dev.bettercode.tasks.application.projects.ProjectCreated
import dev.bettercode.tasks.application.projects.ProjectReopened
import dev.bettercode.tasks.application.tasks.TaskCompleted
import dev.bettercode.tasks.application.tasks.TaskCreated
import dev.bettercode.tasks.application.tasks.TaskReopened
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async

internal open class AuditSpringEventsListener(private val auditService: AuditService) {
    @Suppress("UNUSED_PARAMETER")
    @EventListener
    @Async
    open fun handleTaskCreated(event: TaskCreated) {
        auditService.save(AuditLogFactory.from(event))
    }

    @Suppress("UNUSED_PARAMETER")
    @EventListener
    @Async
    open fun handleProjectCompleted(event: ProjectCreated) {
        auditService.save(AuditLogFactory.from(event))
    }

    @Suppress("UNUSED_PARAMETER")
    @EventListener
    @Async
    fun handleProjectCompleted(event: ProjectCompleted) {
        auditService.save(AuditLogFactory.from(event))
    }

    @Suppress("UNUSED_PARAMETER")
    @EventListener
    @Async
    fun handleProjectReopened(projectReopened: ProjectReopened) {
        auditService.save(AuditLogFactory.from(projectReopened))
    }

    @Suppress("UNUSED_PARAMETER")
    @EventListener
    @Async
    fun handleTaskCompleted(taskCompleted: TaskCompleted) {
        auditService.save(AuditLogFactory.from(taskCompleted))
    }

    @Suppress("UNUSED_PARAMETER")
    @EventListener
    @Async
    fun handleTaskReopened(taskReopened: TaskReopened) {
        auditService.save(AuditLogFactory.from(taskReopened))
    }
}