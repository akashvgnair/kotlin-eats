package com.akashvgnair.kotlinEats.events

import com.akashvgnair.kotlinEats.models.Restaurant
import com.akashvgnair.kotlinEats.types.DomainEvent

sealed class RestaurantEvents : DomainEvent() {
    data class RestaurantCreatedEvent(val restaurant: Restaurant): RestaurantEvents()
    data class RestaurantStatusChangedEvent(val restaurant: Restaurant): RestaurantEvents()
}