package com.akashvgnair.kotlinEats.models

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class CuisineTypeTest {
    @Test
    fun `Has indian cuisine`() {
        assertEquals("INDIAN", CuisineType.INDIAN.toString())
    }

    @Test
    fun `Has chinese cuisine`() {
        assertEquals("CHINESE", CuisineType.CHINESE.toString())
    }

    @Test
    fun `Has italian cuisine`() {
        assertEquals("ITALIAN", CuisineType.ITALIAN.toString())
    }

    @Test
    fun `Has mexican cuisine`() {
        assertEquals("MEXICAN", CuisineType.MEXICAN.toString())
    }

}