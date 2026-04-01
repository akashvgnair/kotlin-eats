package com.akashvgnair.kotlinEats.models

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class RestaurantStatusTest {
    @Test
    fun `Has open restaurant status`() {
        assertEquals("OPEN", RestaurantStatus.OPEN.toString())
    }

    @Test
    fun `Has closed restaurant status`() {
        assertEquals("CLOSED", RestaurantStatus.CLOSED.toString())
    }
}