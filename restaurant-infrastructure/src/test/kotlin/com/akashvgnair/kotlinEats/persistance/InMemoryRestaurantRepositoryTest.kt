package com.akashvgnair.kotlinEats.persistance

import com.akashvgnair.kotlinEats.models.CuisineType
import com.akashvgnair.kotlinEats.models.OperatingHours
import com.akashvgnair.kotlinEats.models.Restaurant
import com.akashvgnair.kotlinEats.types.Address
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalTime

class InMemoryRestaurantRepositoryTest {

    private val repository = InMemoryRestaurantRepository

    private fun createRestaurant(name: String = "Pizza Palace"): Restaurant {
        return Restaurant.from(
            name = name,
            address = Address.from("Calle Mayor 10", "Alicante", "03001", "Spain"),
            cuisineType = CuisineType.ITALIAN,
            operatingHours = OperatingHours.from(LocalTime.of(10, 0), LocalTime.of(22, 0))
        )
    }

    @BeforeEach
    fun setUp() {
        repository.findAll().forEach { repository.delete(it.id) }
    }

    @Test
    fun `save and find by id`() {
        val restaurant = createRestaurant()
        repository.save(restaurant)

        val found = repository.findById(restaurant.id)
        assertNotNull(found)
        assertEquals(restaurant.id, found?.id)
    }

    @Test
    fun `find by id returns null when not found`() {
        val restaurant = createRestaurant()
        assertNull(repository.findById(restaurant.id))
    }

    @Test
    fun `save updates existing restaurant`() {
        val restaurant = createRestaurant()
        repository.save(restaurant)

        restaurant.setName("New Name")
        repository.save(restaurant)

        val found = repository.findById(restaurant.id)
        assertEquals("New Name", found?.name)
        assertEquals(1, repository.findAll().size)
    }

    @Test
    fun `delete removes restaurant`() {
        val restaurant = createRestaurant()
        repository.save(restaurant)

        assertTrue(repository.delete(restaurant.id))
        assertNull(repository.findById(restaurant.id))
    }

    @Test
    fun `delete returns false when not found`() {
        val restaurant = createRestaurant()
        assertFalse(repository.delete(restaurant.id))
    }

    @Test
    fun `find all returns all restaurants`() {
        repository.save(createRestaurant("Pizza Palace"))
        repository.save(createRestaurant("Taco House"))

        assertEquals(2, repository.findAll().size)
    }

    @Test
    fun `find all returns empty when none saved`() {
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    fun `exists by id returns true when exists`() {
        val restaurant = createRestaurant()
        repository.save(restaurant)

        assertTrue(repository.existsById(restaurant.id))
    }

    @Test
    fun `exists by id returns false when not exists`() {
        val restaurant = createRestaurant()
        assertFalse(repository.existsById(restaurant.id))
    }
}