package dev.bettercode.tasks.shared

import dev.bettercode.commons.events.DomainEvent
import org.springframework.context.ApplicationEventPublisher

class SpringEventPublisher(private val eventPublisher: ApplicationEventPublisher) : DomainEventPublisher {
    override fun publish(event: DomainEvent) {
        eventPublisher.publishEvent(event)
    }
}