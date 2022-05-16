package dev.bettercode.audit.service

import dev.bettercode.audit.repository.AuditLog
import dev.bettercode.commons.events.AuditLogCommand

internal class AuditLogFactory{
    companion object {
        fun from(auditLogCommand: AuditLogCommand): AuditLog {
            return AuditLog(msg = auditLogCommand.message, timestamp = auditLogCommand.timestamp)
        }
    }
}
