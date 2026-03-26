package com.akashvgnair.kotlinEats.types

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test


data class Item(val id: String, val name: String, val price: Double)

fun getItemPriceDouble(item: Item?): Result<Double, String> {
    if (item == null) {
        return Result.Failure("Item not found")
    }
    return Result.Success(item.price * 2)
}

class ResultTest {

    @Test
    fun `Return success`() {
        val item: Result<Item, String> = Result.Success(Item("1", "Bread", 12.4))
        assertEquals("Bread", item.getOrNull()?.name)
    }

    @Test
    fun `Return error`() {
        val item: Result<Item, String> = Result.Failure("Item not found")
        assertNull(item.getOrNull())
    }

    @Test
    fun `Return name of the item`() {
        val item: Result<Item, String> = Result.Success(Item("1", "Bread", 12.4))

        val name = item.map { it.name }
        assertEquals("Bread", name.getOrNull())
    }

    @Test
    fun `Return item with double value`() {
        val item: Result<Item, String> = Result.Success(Item("1", "Bread", 12.4))

        val priceResult = item.flatMap { item -> getItemPriceDouble(item) }
        assertEquals(12.4 * 2, priceResult.getOrNull())
    }

    @Test
    fun `Map error`() {
        val result = Result.Failure("Item not found")
        val mapped = result.mapError { RuntimeException(it) }
        assertTrue(mapped is Result.Failure)
        val error = (mapped as Result.Failure).error
        assertEquals("Item not found", error.message)
    }

    @Test
    fun `map on failure passes error through`() {
        val result: Result<Item, String> = Result.Failure("not found")
        val mapped = result.map { it.name }
        assertTrue(mapped is Result.Failure)
        assertEquals("not found", (mapped as Result.Failure).error)
    }

    @Test
    fun `flatMap on failure skips the transform`() {
        val result: Result<Item, String> = Result.Failure("not found")
        val chained = result.flatMap { getItemPriceDouble(it) }
        assertTrue(chained is Result.Failure)
        assertEquals("not found", (chained as Result.Failure).error)
    }

    @Test
    fun `mapError on success passes value through`() {
        val result: Result<Item, String> = Result.Success(Item("1", "Bread", 12.4))
        val mapped = result.mapError { RuntimeException(it) }
        assertTrue(mapped is Result.Success)
        assertEquals("Bread", (mapped as Result.Success).value.name)
    }

    @Test
    fun `getOrElse returns value on success`() {
        val result: Result<Double, String> = Result.Success(10.0)
        assertEquals(10.0, result.getOrElse { 0.0 })
    }

    @Test
    fun `getOrElse returns default on failure`() {
        val result: Result<Double, String> = Result.Failure("error")
        assertEquals(0.0, result.getOrElse { 0.0 })
    }


}