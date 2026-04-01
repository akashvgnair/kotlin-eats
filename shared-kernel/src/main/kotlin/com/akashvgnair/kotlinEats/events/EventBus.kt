package com.akashvgnair.kotlinEats.events

import com.akashvgnair.kotlinEats.types.DomainEvent
import kotlin.reflect.KClass

interface EventBus {
    fun publish(event: DomainEvent)
    fun publishAll(events: List<DomainEvent>)
    fun <T : DomainEvent> subscribe(eventType: KClass<T>, handler: EventHandler<T>)
}