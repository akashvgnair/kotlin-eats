package com.akashvgnair.kotlinEats.types

import org.junit.jupiter.api.Assertions.*
import java.time.Instant
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertNotEquals

class TestClock : Clock {
    override fun now(): Instant {
        return Instant.parse("2026-12-05T10:00:00Z")
    }
}

class TestEvent(private val clock: Clock, id: UUID) : DomainEvent(id) {
    override val occurredOn = clock.now()
}

class OtherTestEvent(private val clock: Clock, id: UUID) : DomainEvent(id) {
    override val occurredOn = clock.now()
}

val knownId: UUID = UUID.randomUUID()

class DomainEventTest {
    @Test
    fun `It displays an event id and passes occurred on correctly`() {
        val clock = TestClock()
        val testEvent = TestEvent(clock, knownId)

        assertNotNull(testEvent.eventId)
        assertEquals(clock.now(), testEvent.occurredOn)
    }

    @Test
    fun `Two events with same UUID are equal`() {
        val clock = TestClock()
        val testEvent1 = TestEvent(clock, knownId)

        assertEquals(knownId, testEvent1.eventId)
        assertEquals(clock.now(), testEvent1.occurredOn)
    }

    @Test
    fun `Two different events with same UUID are not equal`() {
        val clock = TestClock()
        val testEvent1 = TestEvent(clock, knownId)
        val testEvent2 = OtherTestEvent(clock, knownId)

        assertNotEquals<DomainEvent>(testEvent1, testEvent2)
    }

    @Test
    fun `Event prints correct string`() {
        val clock = TestClock()
        val testEvent = TestEvent(clock, knownId)

        assertEquals("TestEvent(eventId=$knownId, occuredOn=${clock.now()})", testEvent.toString())
    }
}