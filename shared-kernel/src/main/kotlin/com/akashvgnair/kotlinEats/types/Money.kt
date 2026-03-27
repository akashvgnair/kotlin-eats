package com.akashvgnair.kotlinEats.types

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency

class Money private constructor(val value: BigDecimal, val currency: Currency) : Comparable<Money> {
    fun isZero() = value.compareTo(BigDecimal.ZERO) == 0

    operator fun plus(money: Money): Money {
        requireSameCurrency(money.currency)
        return Money.from(value.plus(money.value), currency)
    }

    operator fun minus(money: Money): Money {
        requireSameCurrency(money.currency)
        require(value >= money.value) {
            "Cannot subtract ${money.value} from $value: would result in negative money"
        }
        return Money.from(value.minus(money.value), currency)
    }

    operator fun times(multiplicand: Int): Money {
        return Money.from(value.times(BigDecimal.valueOf(multiplicand.toLong())), currency)
    }

    private fun requireSameCurrency(currency: Currency) {
        require(this.currency == currency) {
            "Currency should be same"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Money

        if (value != other.value) return false
        if (currency != other.currency) return false

        return true
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + currency.hashCode()
        return result
    }

    override fun toString(): String {
        return "${currency.currencyCode} $value"
    }

    override fun compareTo(other: Money): Int {
        requireSameCurrency(other.currency)
        return value.compareTo(other.value)
    }

    companion object {


        private fun requirePositiveMoneyValue(value: BigDecimal) {
            require(value >= BigDecimal.ZERO) {
                "Value should be greater than or equal to 0"
            }
        }

        fun fromZero(currency: Currency = Currency.getInstance("EUR")): Money {
            return Money(BigDecimal.ZERO.toFixed(2), currency)
        }

        fun from(value: BigDecimal, currency: Currency = Currency.getInstance("EUR")): Money {
            this.requirePositiveMoneyValue(value)
            return Money(value.toFixed(2), currency)
        }

        fun from(value: Long, currency: Currency = Currency.getInstance("EUR")): Money {
            this.requirePositiveMoneyValue(BigDecimal.valueOf(value))
            return Money(BigDecimal.valueOf(value).toFixed(2), currency)
        }

        fun from(value: Double, currency: Currency = Currency.getInstance("EUR")): Money {
            this.requirePositiveMoneyValue(BigDecimal.valueOf(value))
            return Money(BigDecimal.valueOf(value).toFixed(2), currency)
        }

        fun from(value: Int, currency: Currency = Currency.getInstance("EUR")): Money {
            this.requirePositiveMoneyValue(BigDecimal.valueOf(value.toLong()))
            return Money(BigDecimal.valueOf(value.toLong()).toFixed(2), currency)
        }
    }


}

fun BigDecimal.toFixed(precision: Int = 0): BigDecimal {
    return this.setScale(precision, RoundingMode.HALF_UP)
}