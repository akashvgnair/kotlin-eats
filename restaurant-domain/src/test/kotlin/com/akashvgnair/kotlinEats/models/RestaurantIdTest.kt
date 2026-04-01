package com.akashvgnair.kotlinEats.models

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class RestaurantIdTest {
    val mockId1: UUID = UUID.fromString("a07f8fbb-7b1f-4e99-aa94-37114b5946fe")
    val mockId2: UUID = UUID.fromString("a07f8fbb-7b1f-4e99-aa94-37114b5946ff")

    @Test
    fun `Creates a RestaurantID`() {
        val id = RestaurantId(mockId1)
        assertEquals(mockId1, id.value)
    }

    @Test
    fun `Two RestaurantID with same id is equal`() {
        val id1 = RestaurantId(mockId1)
        val id2 = RestaurantId(mockId1)

        assertEquals(id1, id2)
    }

    @Test
    fun `Two RestaurantID with different id is not equal`() {
        val id1 = RestaurantId(mockId1)
        val id2 = RestaurantId(mockId2)

        assertNotEquals(id1, id2)
    }

    @Test
    fun `To String prints correctly`() {
        val id = RestaurantId(mockId1)
        assertEquals("RestaurantId($mockId1)", id.toString())
    }
}