package com.akashvgnair.kotlinEats.types

import java.util.UUID
import java.time.Instant

abstract class DomainEvent(val eventId: UUID = UUID.randomUUID()) {
    abstract val occurredOn: Instant

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DomainEvent

        return eventId == other.eventId
    }

    override fun hashCode(): Int {
        return eventId.hashCode()
    }

    override fun toString(): String {
        return "${this::class.simpleName}(eventId=$eventId, occuredOn=$occurredOn)"
    }


}