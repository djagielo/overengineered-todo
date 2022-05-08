package dev.bettercode.commons.events

import java.util.*

abstract class DomainEvent{
    fun eventId(): UUID = UUID.randomUUID()
}