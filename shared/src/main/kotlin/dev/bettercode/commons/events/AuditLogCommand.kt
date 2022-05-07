package dev.bettercode.commons.events

data class AuditLogCommand(val message: String): DomainEvent()