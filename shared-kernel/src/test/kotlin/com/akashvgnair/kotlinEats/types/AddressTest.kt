package com.akashvgnair.kotlinEats.types

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class AddressTest {
    @Test
    fun `create address with all fields`() {
        val address = Address.from(
            street = "Calle Mayor 10",
            city = "Alicante",
            postalCode = "03001",
            country = "Spain",
            additionalInfo = "Floor 3, Door B"
        )
        assertEquals("Calle Mayor 10", address.street)
        assertEquals("Alicante", address.city)
        assertEquals("03001", address.postalCode)
        assertEquals("Spain", address.country)
        assertEquals("Floor 3, Door B", address.additionalInfo)
    }

    @Test
    fun `additionalInfo defaults to empty`() {
        val address = Address.from("Calle Mayor 10", "Alicante", "03001", "Spain")
        assertEquals("", address.additionalInfo)
    }

    @Test
    fun `rejects blank street`() {
        assertThrows<IllegalArgumentException> {
            Address.from("", "Alicante", "03001", "Spain")
        }
    }

    @Test
    fun `rejects blank city`() {
        assertThrows<IllegalArgumentException> {
            Address.from("Calle Mayor", "", "03001", "Spain")
        }
    }

    @Test
    fun `rejects blank postalCode`() {
        assertThrows<IllegalArgumentException> {
            Address.from("Calle Mayor", "Alicante", "", "Spain")
        }
    }

    @Test
    fun `rejects blank country`() {
        assertThrows<IllegalArgumentException> {
            Address.from("Calle Mayor", "Alicante", "03001", "")
        }
    }

    @Test
    fun `allows blank additionalInfo`() {
        val address = Address.from("Calle Mayor", "Alicante", "03001", "Spain", "")
        assertEquals("", address.additionalInfo)
    }

    @Test
    fun `same fields are equal`() {
        val a = Address.from("Calle Mayor", "Alicante", "03001", "Spain")
        val b = Address.from("Calle Mayor", "Alicante", "03001", "Spain")
        assertEquals(a, b)
    }

    @Test
    fun `different additionalInfo are not equal`() {
        val a = Address.from("Calle Mayor", "Alicante", "03001", "Spain", "Floor 1")
        val b = Address.from("Calle Mayor", "Alicante", "03001", "Spain", "Floor 5")
        assertNotEquals(a, b)
    }

    @Test
    fun `different street not equal`() {
        val a = Address.from("Calle Mayor", "Alicante", "03001", "Spain")
        val b = Address.from("Calle Menor", "Alicante", "03001", "Spain")
        assertNotEquals(a, b)
    }

    @Test
    fun `equal addresses have same hashCode`() {
        val a = Address.from("Calle Mayor", "Alicante", "03001", "Spain")
        val b = Address.from("Calle Mayor", "Alicante", "03001", "Spain")
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `toString without additionalInfo`() {
        val address = Address.from("Calle Mayor 10", "Alicante", "03001", "Spain")
        assertEquals("Calle Mayor 10, Alicante, 03001, Spain", address.toString())
    }

    @Test
    fun `toString with additionalInfo`() {
        val address = Address.from("Calle Mayor 10", "Alicante", "03001", "Spain", "Floor 3")
        assertEquals("Calle Mayor 10, Alicante, 03001, Spain (Floor 3)", address.toString())
    }
}