package dev.bettercode.tasks.shared

import dev.bettercode.commons.events.DomainEvent

class InMemoryEventPublisher(val events: MutableList<DomainEvent> = mutableListOf()) : DomainEventPublisher {
    override fun publish(event: DomainEvent) {
        this.events.add(event)
    }

    fun clear() {
        this.events.clear()
    }

}