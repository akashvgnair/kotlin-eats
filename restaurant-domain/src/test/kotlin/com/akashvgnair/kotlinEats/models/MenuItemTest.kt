package com.akashvgnair.kotlinEats.models

import com.akashvgnair.kotlinEats.types.Money
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class MenuItemTest {

    private fun createMenuItem(
        name: String = "Butter Chicken",
        category: String = "Main Course",
        price: Money = Money.from(12.99)
    ) = MenuItem.from(name, category, price)

    @Test
    fun `creates a valid menu item`() {
        val item = createMenuItem()
        assertEquals("Butter Chicken", item.name)
        assertEquals("Main Course", item.category)
        assertEquals(Money.from(12.99), item.price)
        assertTrue(item.available)
    }

    @Test
    fun `rejects blank name`() {
        assertThrows<IllegalArgumentException> {
            createMenuItem(name = "")
        }
    }

    @Test
    fun `rejects blank category`() {
        assertThrows<IllegalArgumentException> {
            createMenuItem(category = "")
        }
    }

    @Test
    fun `rejects zero price`() {
        assertThrows<IllegalArgumentException> {
            createMenuItem(price = Money.fromZero())
        }
    }

    @Test
    fun `set name updates name`() {
        val item = createMenuItem()
        item.setName("Paneer Tikka")
        assertEquals("Paneer Tikka", item.name)
    }

    @Test
    fun `set name rejects blank`() {
        val item = createMenuItem()
        assertThrows<IllegalArgumentException> {
            item.setName("")
        }
    }

    @Test
    fun `set category updates category`() {
        val item = createMenuItem()
        item.setCategory("Starters")
        assertEquals("Starters", item.category)
    }

    @Test
    fun `set category rejects blank`() {
        val item = createMenuItem()
        assertThrows<IllegalArgumentException> {
            item.setCategory("")
        }
    }

    @Test
    fun `set price updates price`() {
        val item = createMenuItem()
        val newPrice = Money.from(15.99)
        item.setPrice(newPrice)
        assertEquals(newPrice, item.price)
    }

    @Test
    fun `set price rejects zero`() {
        val item = createMenuItem()
        assertThrows<IllegalArgumentException> {
            item.setPrice(Money.fromZero())
        }
    }

    @Test
    fun `set available to false`() {
        val item = createMenuItem()
        item.setAvailable(false)
        assertFalse(item.available)
    }

    @Test
    fun `set available back to true`() {
        val item = createMenuItem()
        item.setAvailable(false)
        item.setAvailable(true)
        assertTrue(item.available)
    }

    @Test
    fun `two items with same id are equal`() {
        val item = createMenuItem()
        assertEquals(item, item)
    }

    @Test
    fun `two items with different id are not equal`() {
        val item1 = createMenuItem()
        val item2 = createMenuItem()
        assertNotEquals(item1, item2)
    }
}