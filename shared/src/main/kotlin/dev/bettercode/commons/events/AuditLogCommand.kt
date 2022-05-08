package dev.bettercode.commons.events

import java.time.Instant

data class AuditLogCommand(val message: String, val timestamp: Instant = Instant.now()) : DomainEvent()