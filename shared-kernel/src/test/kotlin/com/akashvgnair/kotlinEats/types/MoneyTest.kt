package com.akashvgnair.kotlinEats.types

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import java.util.Currency
import kotlin.test.Test

class MoneyTest {
    @Test
    fun `create money from double`() {
        val money = Money.from(10.50)
        assertEquals("10.50", money.value.toPlainString())
        assertEquals(Currency.getInstance("EUR"), money.currency)
    }

    @Test
    fun `create money from int`() {
        val money = Money.from(10)
        assertEquals("10.00", money.value.toPlainString())
    }

    @Test
    fun `create money from long`() {
        val money = Money.from(100L)
        assertEquals("100.00", money.value.toPlainString())
    }

    @Test
    fun `create money with specific currency`() {
        val money = Money.from(10.0, Currency.getInstance("USD"))
        assertEquals(Currency.getInstance("USD"), money.currency)
    }

    @Test
    fun `create zero money`() {
        val money = Money.fromZero()
        assertTrue(money.isZero())
    }

    @Test
    fun `non-zero money is not zero`() {
        assertFalse(Money.from(1.0).isZero())
    }

    @Test
    fun `rejects negative value`() {
        assertThrows<IllegalArgumentException> {
            Money.from(-5.0)
        }
    }

    @Test
    fun `allows zero value`() {
        val money = Money.from(0.0)
        assertTrue(money.isZero())
    }

    // Addition

    @Test
    fun `add two money of same currency`() {
        val result = Money.from(10.0) + Money.from(5.50)
        assertEquals(Money.from(15.50), result)
    }

    @Test
    fun `add fails with different currencies`() {
        assertThrows<IllegalArgumentException> {
            Money.from(10.0) + Money.from(5.0, Currency.getInstance("USD"))
        }
    }

    // Subtraction

    @Test
    fun `subtract money of same currency`() {
        val result = Money.from(10.0) - Money.from(3.50)
        assertEquals(Money.from(6.50), result)
    }

    @Test
    fun `subtract equal amounts gives zero`() {
        val result = Money.from(10.0) - Money.from(10.0)
        assertTrue(result.isZero())
    }

    @Test
    fun `subtract fails when result would be negative`() {
        assertThrows<IllegalArgumentException> {
            Money.from(5.0) - Money.from(10.0)
        }
    }

    @Test
    fun `subtract fails with different currencies`() {
        assertThrows<IllegalArgumentException> {
            Money.from(10.0) - Money.from(5.0, Currency.getInstance("USD"))
        }
    }

    // Multiplication

    @Test
    fun `multiply by integer`() {
        val result = Money.from(7.50) * 3
        assertEquals(Money.from(22.50), result)
    }

    @Test
    fun `multiply by zero gives zero`() {
        val result = Money.from(10.0) * 0
        assertTrue(result.isZero())
    }

    @Test
    fun `multiply by negative fails`() {
        assertThrows<IllegalArgumentException> {
            Money.from(10.0) * -2
        }
    }

    // Equality

    @Test
    fun `same amount and currency are equal`() {
        assertEquals(Money.from(10.0), Money.from(10.0))
    }

    @Test
    fun `different amounts are not equal`() {
        assertFalse(Money.from(10.0) == Money.from(20.0))
    }

    @Test
    fun `different currencies are not equal`() {
        val eur = Money.from(10.0)
        val usd = Money.from(10.0, Currency.getInstance("USD"))
        assertFalse(eur == usd)
    }

    @Test
    fun `equal money has same hashCode`() {
        assertEquals(Money.from(10.0).hashCode(), Money.from(10.0).hashCode())
    }

    // toString

    @Test
    fun `toString shows currency and amount`() {
        val money = Money.from(42.99)
        assertEquals("EUR 42.99", money.toString())
    }

    // Rounding

    @Test
    fun `rounds to 2 decimal places`() {
        val money = Money.from(10.555)
        assertEquals("10.56", money.value.toPlainString())
    }

    @Test
    fun `less than comparison`() {
        assertTrue(Money.from(5.0) < Money.from(10.0))
    }

    @Test
    fun `greater than comparison`() {
        assertTrue(Money.from(10.0) > Money.from(5.0))
    }

    @Test
    fun `equal amounts compare to zero`() {
        assertEquals(0, Money.from(10.0).compareTo(Money.from(10.0)))
    }

    @Test
    fun `compareTo fails with different currencies`() {
        assertThrows<IllegalArgumentException> {
            Money.from(10.0).compareTo(Money.from(5.0, Currency.getInstance("USD")))
        }
    }
}