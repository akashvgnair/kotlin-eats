package com.akashvgnair.kotlinEats.specification

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class Even : Specification<Int> {
    override fun isSatisfiedBy(candidate: Int): Boolean {
        return candidate % 2 == 0
    }
}

class Positive : Specification<Int> {
    override fun isSatisfiedBy(candidate: Int): Boolean {
        return candidate > 0
    }
}


class SpecificationTest {
    @Test
    fun `and returns true when both conditions are true`() {
        val evenAndPositive = Even() and Positive()

        val isEvenAndPositive = evenAndPositive.isSatisfiedBy(2)
        assertTrue {
            isEvenAndPositive
        }
    }

    @Test
    fun `and returns false when one conditions is false`() {
        val evenAndPositive = Even() and Positive()

        val isEvenAndPositive = evenAndPositive.isSatisfiedBy(3)
        assertFalse {
            isEvenAndPositive
        }
    }

    @Test
    fun `or returns true when one conditions is true`() {
        val evenOrPositive = Even() or Positive()

        val isEvenOrPositive = evenOrPositive.isSatisfiedBy(3)
        assertTrue {
            isEvenOrPositive
        }
    }

    @Test
    fun `or returns false when all conditions are false`() {
        val evenOrPositive = Even() and Positive()

        val isEvenOrPositive = evenOrPositive.isSatisfiedBy(-3)
        assertFalse {
            isEvenOrPositive
        }
    }

    @Test
    fun `not returns false when number is even`() {
        val even = Even()

        val isEven = even.isSatisfiedBy(2)
        assertFalse {
            !isEven
        }
    }

    @Test
    fun `not returns true when number is odd`() {
        val even = Even()

        val isEven = even.isSatisfiedBy(3)
        assertTrue {
            !isEven
        }
    }

}