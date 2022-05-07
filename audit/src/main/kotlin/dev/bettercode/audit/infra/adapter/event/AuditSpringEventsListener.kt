package dev.bettercode.audit.infra.adapter.event

import dev.bettercode.audit.service.AuditLogFactory
import dev.bettercode.audit.service.AuditService
import dev.bettercode.commons.events.AuditLogCommand
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async

internal open class AuditSpringEventsListener(private val auditService: AuditService) {

    @Suppress("UNUSED_PARAMETER")
    @EventListener
    @Async
    fun handleAuditLogCmd(auditLogCommand: AuditLogCommand) {
        auditService.save(AuditLogFactory.from(auditLogCommand))
    }
}