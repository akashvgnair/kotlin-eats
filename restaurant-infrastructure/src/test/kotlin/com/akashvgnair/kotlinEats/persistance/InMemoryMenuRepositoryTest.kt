package com.akashvgnair.kotlinEats.persistance

import com.akashvgnair.kotlinEats.models.Menu
import com.akashvgnair.kotlinEats.models.MenuItem
import com.akashvgnair.kotlinEats.models.RestaurantId
import com.akashvgnair.kotlinEats.types.Money
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InMemoryMenuRepositoryTest {

    private val menuRepository = InMemoryMenuRepository
    private val restaurantId = RestaurantId()

    @BeforeEach
    fun setUp() {
        menuRepository.findAll().forEach { menuRepository.delete(it.id) }
    }

    @Test
    fun `save and find by id`() {
        val menu = Menu.from(restaurantId)
        menuRepository.save(menu)

        val found = menuRepository.findById(menu.id)
        assertNotNull(found)
        assertEquals(menu.id, found?.id)
    }

    @Test
    fun `find by id returns null when not found`() {
        val menu = Menu.from(restaurantId)
        assertNull(menuRepository.findById(menu.id))
    }

    @Test
    fun `find by restaurant id`() {
        val menu = Menu.from(restaurantId)
        menuRepository.save(menu)

        val found = menuRepository.findByRestaurantId(restaurantId)
        assertNotNull(found)
        assertEquals(restaurantId, found?.restaurantId)
    }

    @Test
    fun `find by restaurant id returns null when not found`() {
        assertNull(menuRepository.findByRestaurantId(RestaurantId()))
    }

    @Test
    fun `save with menu items`() {
        val menu = Menu.from(restaurantId)
        menu.addMenuItem(MenuItem.from("Margherita", "Pizza", Money.from(10)))
        menuRepository.save(menu)

        val found = menuRepository.findById(menu.id)
        assertEquals(1, found?.getAllMenuItems()?.size)
    }

    @Test
    fun `delete removes menu`() {
        val menu = Menu.from(restaurantId)
        menuRepository.save(menu)

        assertTrue(menuRepository.delete(menu.id))
        assertNull(menuRepository.findById(menu.id))
    }

    @Test
    fun `delete returns false when not found`() {
        val menu = Menu.from(restaurantId)
        assertFalse(menuRepository.delete(menu.id))
    }

    @Test
    fun `find all returns all menus`() {
        val menu1 = Menu.from(RestaurantId())
        val menu2 = Menu.from(RestaurantId())
        menuRepository.save(menu1)
        menuRepository.save(menu2)

        assertEquals(2, menuRepository.findAll().size)
    }

    @Test
    fun `exists by id returns true when exists`() {
        val menu = Menu.from(restaurantId)
        menuRepository.save(menu)

        assertTrue(menuRepository.existsById(menu.id))
    }

    @Test
    fun `exists by id returns false when not exists`() {
        val menu = Menu.from(restaurantId)
        assertFalse(menuRepository.existsById(menu.id))
    }
}