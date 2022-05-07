package dev.bettercode.tasks.shared

import dev.bettercode.commons.events.DomainEvent

interface DomainEventPublisher {
    fun publish(event: DomainEvent)
    fun publish(events: List<DomainEvent>) {
        events.forEach { publish(it) }
    }
}