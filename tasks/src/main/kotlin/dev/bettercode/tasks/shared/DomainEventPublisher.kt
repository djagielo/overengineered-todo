package dev.bettercode.tasks.shared

interface DomainEventPublisher {
    fun publish(event: DomainEvent)
    fun publish(events: List<DomainEvent>) {
        events.forEach { publish(it) }
    }
}