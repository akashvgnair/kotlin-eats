package com.akashvgnair.kotlinEats.models

import com.akashvgnair.kotlinEats.events.MenuEvents
import com.akashvgnair.kotlinEats.types.AggregateRoot
import com.akashvgnair.kotlinEats.types.Money

class Menu private constructor(id: MenuId, val restaurantId: RestaurantId) :
    AggregateRoot<MenuId>(id) {

    private val items: MutableMap<MenuItemId, MenuItem> = mutableMapOf()

    fun getAllMenuItems(): List<MenuItem> = items.values.toList()

    fun addMenuItem(menuItem: MenuItem) {
        require(getAllMenuItems().none() { it.name.equals(menuItem.name, ignoreCase = true) }) {
            "Menu item with the same name already exists."
        }
        items[menuItem.id] = menuItem
        this.addEvent(MenuEvents.MenuItemAddedEvent(menuItem))
    }

    fun removeMenuItem(itemId: MenuItemId) {
        val menuItem = getMenuItemByMenuItemId(itemId) ?: throw NoSuchElementException("Item not found")
        items.remove(menuItem.id)
        this.addEvent(MenuEvents.MenuItemRemovedEvent(menuItem))
    }

    fun getMenuItemByMenuItemId(menuItemId: MenuItemId): MenuItem? {
        return items[menuItemId]
    }

    fun getAvailableItems(): List<MenuItem> {
        return items.values.toList().filter { it.available }
    }

    fun updateMenuItemPrice(itemId: MenuItemId, price: Money) {
        val menuItem = getMenuItemByMenuItemId(itemId) ?: throw NoSuchElementException("Item not found")
        menuItem.setPrice(price)
        this.addEvent(MenuEvents.MenuItemPriceChangedEvent(menuItem))
    }

    fun toggleMenuItemAvailability(itemId: MenuItemId) {
        val menuItem = getMenuItemByMenuItemId(itemId) ?: throw NoSuchElementException("Item not found")
        menuItem.setAvailable(!menuItem.available)
        this.addEvent(MenuEvents.ItemAvailabilityChangedEvent(menuItem))
    }

    companion object {
        fun from(restaurantId: RestaurantId): Menu {
            return Menu(MenuId(), restaurantId)
        }
    }
}