package dev.bettercode.tasks.shared

class InMemoryEventPublisher(val events: MutableList<DomainEvent> = mutableListOf()) : DomainEventPublisher {
    override fun publish(event: DomainEvent) {
        this.events.add(event)
    }

    fun clear() {
        this.events.clear()
    }

}