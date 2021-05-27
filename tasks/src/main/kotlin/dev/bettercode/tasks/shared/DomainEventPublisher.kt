package dev.bettercode.tasks.shared

interface DomainEventPublisher {
    fun publish(event: DomainEvent)
}