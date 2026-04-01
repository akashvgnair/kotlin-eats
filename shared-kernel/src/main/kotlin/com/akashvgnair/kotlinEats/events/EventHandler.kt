package com.akashvgnair.kotlinEats.events

import com.akashvgnair.kotlinEats.types.DomainEvent

interface EventHandler<T : DomainEvent> {
    fun handle(event: T)
}