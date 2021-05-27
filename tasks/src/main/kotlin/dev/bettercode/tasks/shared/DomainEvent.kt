package dev.bettercode.tasks.shared

import java.util.*

abstract class DomainEvent {
    abstract fun eventId(): UUID
}