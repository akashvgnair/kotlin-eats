package com.akashvgnair.kotlinEats.models

import com.akashvgnair.kotlinEats.specification.Specification
import java.time.LocalTime


internal class OpenTimeSpecification(val openTime: LocalTime) : Specification<LocalTime> {
    override fun isSatisfiedBy(candidate: LocalTime): Boolean {
        return openTime.isBefore(candidate)
    }
}

internal class CloseTimeSpecification(val closeTime: LocalTime) : Specification<LocalTime> {
    override fun isSatisfiedBy(candidate: LocalTime): Boolean {
        return closeTime.isAfter(candidate)
    }
}

class OperatingHours private constructor(val openTime: LocalTime, val closeTime: LocalTime) {
    private val openTimeSpecification = OpenTimeSpecification(openTime)
    private val closeTimeSpecification = CloseTimeSpecification(closeTime)
    private val inBetweenTimeSpecification = openTimeSpecification and closeTimeSpecification

    fun isTimeWithinHours(currentTime: LocalTime): Boolean = inBetweenTimeSpecification.isSatisfiedBy(currentTime)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OperatingHours

        if (openTime != other.openTime) return false
        if (closeTime != other.closeTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = openTime.hashCode()
        result = 31 * result + closeTime.hashCode()
        return result
    }

    override fun toString(): String {
        return "OperatingHours(openTime=${openTime}, closeTime=${closeTime})"
    }

    companion object {
        private const val INVALID_OPENING_HOURS_MESSAGE = "Open time should be before close time"
        fun from(openTime: LocalTime, closeTime: LocalTime): OperatingHours {
            check(openTime.isBefore(closeTime)) {
                INVALID_OPENING_HOURS_MESSAGE
            }
            return OperatingHours(openTime, closeTime)
        }
    }
}