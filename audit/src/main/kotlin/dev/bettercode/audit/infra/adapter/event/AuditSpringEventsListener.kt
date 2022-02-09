package dev.bettercode.audit.infra.adapter.event

import dev.bettercode.audit.service.AuditLogFactory
import dev.bettercode.audit.service.AuditService
import dev.bettercode.tasks.application.projects.ProjectCreated
import dev.bettercode.tasks.application.tasks.TaskCreated
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
    open fun handleProjectCreated(event: ProjectCreated) {
        auditService.save(AuditLogFactory.from(event))
    }
}