package com.akashvgnair.kotlinEats.types

import org.junit.jupiter.api.Assertions.*
import java.util.UUID
import kotlin.test.Test


class TestEntityId(id: UUID = UUID.randomUUID()) : EntityId(id)

class TestDomainEvent : DomainEvent(UUID.randomUUID())

class TestAggregate(id: TestEntityId) : AggregateRoot<TestEntityId>(id)

val testEntityId1 = TestEntityId(UUID.fromString("a07f8fbb-7b1f-4e99-aa94-37114b5946fe"))
val testEntityId2 = TestEntityId()

class AggregateRootTest {
    @Test
    fun `Create a new Aggregate Root`() {
        val aggregateRoot = TestAggregate(testEntityId1)
        assertEquals("a07f8fbb-7b1f-4e99-aa94-37114b5946fe", aggregateRoot.id.value.toString())
    }

    @Test
    fun `Two aggregate roots are not equal`() {
        val aggregateRoot1 = TestAggregate(testEntityId1)
        val aggregateRoot2 = TestAggregate(testEntityId2)

        assertNotEquals(aggregateRoot1, aggregateRoot2)
    }

    @Test
    fun `Two aggregate roots with same id is equal`() {
        val aggregateRoot1 = TestAggregate(testEntityId1)
        val aggregateRoot2 = TestAggregate(testEntityId1)

        assertEquals(aggregateRoot1, aggregateRoot2)
    }

    @Test
    fun `Two string prints the aggregate root correctly`() {
        val aggregateRoot = TestAggregate(testEntityId1)

        assertEquals("AggregateRoot<TestAggregate> = a07f8fbb-7b1f-4e99-aa94-37114b5946fe)", aggregateRoot.toString())
    }

    @Test
    fun `Add domain events to aggregate root`() {
        val testEvent1 = TestDomainEvent()
        val aggregateRoot = TestAggregate(testEntityId1)
        aggregateRoot.addEvent(testEvent1)

        assertEquals(testEvent1, aggregateRoot.getDomainEvents().first())
    }

    @Test
    fun `Clears domain events in aggregate root`() {
        val testEvent1 = TestDomainEvent()
        val aggregateRoot = TestAggregate(testEntityId1)
        aggregateRoot.addEvent(testEvent1)
        val events = aggregateRoot.clearEvents()

        assertEquals(listOf(testEvent1), events)
        assertEquals(emptyList<DomainEvent>(), aggregateRoot.getDomainEvents())
    }

}