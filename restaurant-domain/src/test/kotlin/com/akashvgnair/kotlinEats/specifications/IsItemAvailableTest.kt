package com.akashvgnair.kotlinEats.specifications

import com.akashvgnair.kotlinEats.models.Menu
import com.akashvgnair.kotlinEats.models.MenuItem
import com.akashvgnair.kotlinEats.models.RestaurantId
import com.akashvgnair.kotlinEats.types.Money
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class IsItemAvailableTest {
    lateinit var menu: Menu
    val item: MenuItem = MenuItem.from(
        name = "Pesto",
        category = "Pasta",
        price = Money.from(15)
    )

    @BeforeEach
    fun setup() {
        val restaurantId = RestaurantId()
        menu = Menu.from(restaurantId)
        menu.addMenuItem(item)
    }

    @Test
    fun `Return true when item is available`() {
        assertTrue {
            IsItemAvailable(item).isSatisfiedBy(menu)
        }
    }

    @Test
    fun `Return false when item is not available`() {
        menu.toggleMenuItemAvailability(item.id)

        assertFalse {
            IsItemAvailable(item).isSatisfiedBy(menu)
        }
    }

    @Test
    fun `Return false when item is available but is a different item`() {
        val newItem: MenuItem = MenuItem.from(
        name = "Pink sauce",
        category = "Pasta",
        price = Money.from(19)
        )

        assertFalse {
            IsItemAvailable(newItem).isSatisfiedBy(menu)
        }
    }




}