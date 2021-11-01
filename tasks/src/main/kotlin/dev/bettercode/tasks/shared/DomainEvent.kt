package dev.bettercode.tasks.shared

import java.util.*

abstract class DomainEvent {
    fun eventId(): UUID = UUID.randomUUID()
}