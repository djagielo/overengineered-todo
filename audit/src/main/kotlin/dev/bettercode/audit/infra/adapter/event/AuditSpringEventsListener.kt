package dev.bettercode.audit.infra.adapter.event

import dev.bettercode.audit.service.AuditLogFactory
import dev.bettercode.audit.service.AuditService
import dev.bettercode.commons.events.AuditLogCommand
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
open class AuditSpringEventsListener(private val auditService: AuditService) {

    @EventListener
    @Async
    fun handleAuditLogCmd(auditLogCommand: AuditLogCommand) {
        auditService.save(AuditLogFactory.from(auditLogCommand))
    }
}