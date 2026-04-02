package com.akashvgnair.kotlinEats.models

import com.akashvgnair.kotlinEats.events.RestaurantEvents
import com.akashvgnair.kotlinEats.types.Address
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RestaurantTest {

    private lateinit var restaurant: Restaurant

    private val address = Address.from("Calle Mayor 10", "Alicante", "03001", "Spain")
    private val operatingHours = OperatingHours.from(LocalTime.of(10, 0), LocalTime.of(22, 0))

    @BeforeEach
    fun setUp() {
        restaurant = Restaurant.from("Pizza Palace", address, CuisineType.ITALIAN, operatingHours)
    }

    @Test
    fun `creates restaurant with correct fields`() {
        assertEquals("Pizza Palace", restaurant.name)
        assertEquals(address, restaurant.address)
        assertEquals(CuisineType.ITALIAN, restaurant.cuisineType)
        assertEquals(operatingHours, restaurant.operatingHours)
        assertEquals(RestaurantStatus.CLOSED, restaurant.status)
    }

    @Test
    fun `publishes created event on creation`() {
        assertTrue {
            restaurant.getDomainEvents().any { it is RestaurantEvents.RestaurantCreatedEvent }
        }
    }

    @Test
    fun `rejects blank name`() {
        assertThrows<IllegalArgumentException> {
            Restaurant.from("", address, CuisineType.ITALIAN, operatingHours)
        }
    }

    @Test
    fun `opens restaurant`() {
        restaurant.open()
        assertEquals(RestaurantStatus.OPEN, restaurant.status)
        assertTrue {
            restaurant.getDomainEvents().any { it is RestaurantEvents.RestaurantStatusChangedEvent }
        }
    }

    @Test
    fun `cannot open already open restaurant`() {
        restaurant.open()
        val exception = assertThrows<IllegalArgumentException> {
            restaurant.open()
        }
        assertEquals("Restaurant is already open", exception.message)
    }

    @Test
    fun `closes restaurant`() {
        restaurant.open()
        restaurant.close()
        assertEquals(RestaurantStatus.CLOSED, restaurant.status)
    }

    @Test
    fun `cannot close already closed restaurant`() {
        val exception = assertThrows<IllegalArgumentException> {
            restaurant.close()
        }
        assertEquals("Restaurant is already closed", exception.message)
    }

    @Test
    fun `isOpen true when status OPEN and within hours`() {
        restaurant.open()
        assertTrue(restaurant.isOpen(LocalTime.of(14, 0)))
    }

    @Test
    fun `isOpen false when status OPEN but outside hours`() {
        restaurant.open()
        assertFalse(restaurant.isOpen(LocalTime.of(23, 0)))
    }

    @Test
    fun `isOpen false when within hours but status CLOSED`() {
        assertFalse(restaurant.isOpen(LocalTime.of(14, 0)))
    }

    @Test
    fun `updates name`() {
        restaurant.setName("Pasta House")
        assertEquals("Pasta House", restaurant.name)
    }

    @Test
    fun `rejects blank name update`() {
        assertThrows<IllegalArgumentException> {
            restaurant.setName("")
        }
    }

    @Test
    fun `updates address`() {
        val newAddress = Address.from("Calle Nueva 5", "Madrid", "28001", "Spain")
        restaurant.setAddress(newAddress)
        assertEquals(newAddress, restaurant.address)
    }

    @Test
    fun `updates cuisine type`() {
        restaurant.setCuisineType(CuisineType.MEXICAN)
        assertEquals(CuisineType.MEXICAN, restaurant.cuisineType)
    }

    @Test
    fun `updates operating hours`() {
        val newHours = OperatingHours.from(LocalTime.of(8, 0), LocalTime.of(20, 0))
        restaurant.setOperatingHours(newHours)
        assertEquals(newHours, restaurant.operatingHours)
    }
}