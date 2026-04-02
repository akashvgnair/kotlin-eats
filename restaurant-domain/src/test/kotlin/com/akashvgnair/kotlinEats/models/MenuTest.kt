package com.akashvgnair.kotlinEats.models

import com.akashvgnair.kotlinEats.events.MenuEvents
import com.akashvgnair.kotlinEats.types.Money
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Currency



fun addItem(i: Int = 0, menu: Menu) {
    menu.addMenuItem(MenuItem.from("Margarita $i", "Pizza", Money.from(12.99)))
}

class MenuTest {
    private lateinit var menu: Menu

    @BeforeEach
    fun setUp() {
        val restaurantId = RestaurantId()
        menu = Menu.from(restaurantId)
        addItem(0, menu)
    }

    @Test
    fun `Adds a new Menu item`() {
        assertEquals("Margarita 0", menu.getAllMenuItems().first().name)
        assertEquals("Pizza", menu.getAllMenuItems().first().category)
        assertEquals(Money.from(12.99), menu.getAllMenuItems().first().price)
        assertTrue { menu.getAllMenuItems().first().available }
        assertFalse { menu.getAvailableItems().isEmpty() }

        assertTrue {
            menu.getDomainEvents().any() {
                it is MenuEvents.MenuItemAddedEvent
            }
        }
    }

    @Test
    fun `Removes a Menu item`() {
        val id = menu.getAllMenuItems().first().id
        menu.removeMenuItem(id)
        assertTrue { menu.getAllMenuItems().isEmpty() }
        assertTrue {
            menu.getDomainEvents().any() {
                it is MenuEvents.MenuItemRemovedEvent
            }
        }
        val exception = assertThrows<NoSuchElementException> {
            menu.removeMenuItem(id)
        }
        assertEquals("Item not found", exception.message)
    }

    @Test
    fun `Updates the price of the menu item`() {
        menu.updateMenuItemPrice(menu.getAllMenuItems().first().id, Money.from(10))
        assertEquals(Money.from(10), menu.getAllMenuItems().first().price)
        assertTrue {
            menu.getDomainEvents().any {
                it is MenuEvents.MenuItemPriceChangedEvent
            }
        }
    }

    @Test
    fun `Throws exception when the price of the menu item is in different currency`() {
        val exception = assertThrows<IllegalArgumentException> {
            menu.updateMenuItemPrice(menu.getAllMenuItems().first().id, Money.from(10, Currency.getInstance("USD")))
        }
        assertEquals("Currency should be same", exception.message)
        assertEquals(Money.from(12.99), menu.getAllMenuItems().first().price)
        assertFalse {
            menu.getDomainEvents().any {
                it is MenuEvents.MenuItemPriceChangedEvent
            }
        }
    }

    @Test
    fun `Throws exception when the menu item already added`() {
        val exception = assertThrows<IllegalArgumentException> {
            addItem(0, menu)
        }
        assertEquals("Menu item with the same name already exists.", exception.message)

    }

    @Test
    fun `Updates the availability of the menu item`() {
        menu.toggleMenuItemAvailability(menu.getAllMenuItems().first().id)
        assertFalse { menu.getAllMenuItems().first().available }
        assertTrue { menu.getAvailableItems().isEmpty() }
        assertTrue {
            menu.getDomainEvents().any {
                it is MenuEvents.ItemAvailabilityChangedEvent
            }
        }
    }

}