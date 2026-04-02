package com.akashvgnair.kotlinEats.usecase.fixtures

import com.akashvgnair.kotlinEats.events.EventBus
import com.akashvgnair.kotlinEats.events.EventHandler
import com.akashvgnair.kotlinEats.types.DomainEvent
import kotlin.reflect.KClass

class MockEventBus : EventBus {
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