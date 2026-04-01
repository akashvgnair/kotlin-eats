package com.akashvgnair.kotlinEats.events

import com.akashvgnair.kotlinEats.types.DomainEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass


class InMemoryEventBus : EventBus {
    val memoryEventBus: MutableMap<KClass<*>, MutableList<EventHandler<*>>> = mutableMapOf()

    override fun publish(event: DomainEvent) {
        val eventHandlers = memoryEventBus[event::class]
        eventHandlers?.forEach {
            @Suppress("UNCHECKED_CAST")
            (it as EventHandler<DomainEvent>).handle(event)
        }
    }

    override fun publishAll(events: List<DomainEvent>) {
        events.forEach { publish(it) }
    }

    override fun <T : DomainEvent> subscribe(eventType: KClass<T>, handler: EventHandler<T>) {
        memoryEventBus.getOrPut(eventType) { mutableListOf() }.add(handler)
    }
}

class OrderPlacedEvent(val orderId: String) : DomainEvent()
class PaymentReceivedEvent(val amount: Double) : DomainEvent()


class InMemoryEventBusTest {
    @Test
    fun `handler receives published event`() {
        val bus = InMemoryEventBus()
        val received = mutableListOf<OrderPlacedEvent>()

        bus.subscribe(OrderPlacedEvent::class, object : EventHandler<OrderPlacedEvent> {
            override fun handle(event: OrderPlacedEvent) {
                received.add(event)
            }
        })

        bus.publish(OrderPlacedEvent("order-1"))

        assertEquals(1, received.size)
        assertEquals("order-1", received[0].orderId)
    }

    @Test
    fun `multiple handlers receive the same event`() {
        val bus = InMemoryEventBus()
        val handlerA = mutableListOf<OrderPlacedEvent>()
        val handlerB = mutableListOf<OrderPlacedEvent>()

        bus.subscribe(OrderPlacedEvent::class, object : EventHandler<OrderPlacedEvent> {
            override fun handle(event: OrderPlacedEvent) { handlerA.add(event) }
        })
        bus.subscribe(OrderPlacedEvent::class, object : EventHandler<OrderPlacedEvent> {
            override fun handle(event: OrderPlacedEvent) { handlerB.add(event) }
        })

        bus.publish(OrderPlacedEvent("order-1"))

        assertEquals(1, handlerA.size)
        assertEquals(1, handlerB.size)
    }

    @Test
    fun `handler only receives its subscribed event type`() {
        val bus = InMemoryEventBus()
        val orderEvents = mutableListOf<OrderPlacedEvent>()
        val paymentEvents = mutableListOf<PaymentReceivedEvent>()

        bus.subscribe(OrderPlacedEvent::class, object : EventHandler<OrderPlacedEvent> {
            override fun handle(event: OrderPlacedEvent) { orderEvents.add(event) }
        })
        bus.subscribe(PaymentReceivedEvent::class, object : EventHandler<PaymentReceivedEvent> {
            override fun handle(event: PaymentReceivedEvent) { paymentEvents.add(event) }
        })

        bus.publish(OrderPlacedEvent("order-1"))
        bus.publish(PaymentReceivedEvent(25.0))

        assertEquals(1, orderEvents.size)
        assertEquals(1, paymentEvents.size)
    }

    @Test
    fun `publishing with no subscribers does not throw`() {
        val bus = InMemoryEventBus()
        bus.publish(OrderPlacedEvent("order-1"))
    }

    @Test
    fun `publishAll delivers all events`() {
        val bus = InMemoryEventBus()
        val orderEvents = mutableListOf<OrderPlacedEvent>()
        val paymentEvents = mutableListOf<PaymentReceivedEvent>()

        bus.subscribe(OrderPlacedEvent::class, object : EventHandler<OrderPlacedEvent> {
            override fun handle(event: OrderPlacedEvent) { orderEvents.add(event) }
        })
        bus.subscribe(PaymentReceivedEvent::class, object : EventHandler<PaymentReceivedEvent> {
            override fun handle(event: PaymentReceivedEvent) { paymentEvents.add(event) }
        })

        bus.publishAll(listOf(
            OrderPlacedEvent("order-1"),
            PaymentReceivedEvent(50.0),
            OrderPlacedEvent("order-2")
        ))

        assertEquals(2, orderEvents.size)
        assertEquals(1, paymentEvents.size)
    }

    @Test
    fun `events published before subscription are not delivered`() {
        val bus = InMemoryEventBus()

        bus.publish(OrderPlacedEvent("order-1"))

        val received = mutableListOf<OrderPlacedEvent>()
        bus.subscribe(OrderPlacedEvent::class, object : EventHandler<OrderPlacedEvent> {
            override fun handle(event: OrderPlacedEvent) { received.add(event) }
        })

        assertTrue(received.isEmpty())
    }
}