package com.akashvgnair.kotlinEats.events

import com.akashvgnair.kotlinEats.models.MenuItem
import com.akashvgnair.kotlinEats.types.DomainEvent

sealed class MenuEvents: DomainEvent() {
    data class MenuItemAddedEvent(val menuItem: MenuItem) : MenuEvents()
    data class MenuItemRemovedEvent(val menuItem: MenuItem) : MenuEvents()
    data class ItemAvailabilityChangedEvent(val menuItem: MenuItem) : MenuEvents()
    data class MenuItemPriceChangedEvent(val menuItem: MenuItem) : MenuEvents()
}