package com.akashvgnair.kotlinEats.models

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class MenuIdTest {
    val mockId1: UUID = UUID.fromString("a07f8fbb-7b1f-4e99-aa94-37114b5946fe")
    val mockId2: UUID = UUID.fromString("a07f8fbb-7b1f-4e99-aa94-37114b5946ff")

    @Test
    fun `Creates a MenuId`() {
        val id = MenuId(mockId1)
        assertEquals(mockId1, id.value)
    }

    @Test
    fun `Two MenuId with same id is equal`() {
        val id1 = MenuId(mockId1)
        val id2 = MenuId(mockId1)

        assertEquals(id1, id2)
    }

    @Test
    fun `Two MenuId with different id is not equal`() {
        val id1 = MenuId(mockId1)
        val id2 = MenuId(mockId2)

        assertNotEquals(id1, id2)
    }

    @Test
    fun `To String prints correctly`() {
        val id = MenuId(mockId1)
        assertEquals("MenuId($mockId1)", id.toString())
    }
}