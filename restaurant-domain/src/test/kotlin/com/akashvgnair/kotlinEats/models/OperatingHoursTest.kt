package com.akashvgnair.kotlinEats.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalTime

val openTime: LocalTime = LocalTime.of(10, 0)
val closeTime: LocalTime = LocalTime.of(16, 0)
val duringHours: LocalTime = LocalTime.of(13, 0)
val afterHours: LocalTime = LocalTime.of(23, 0)

class OperatingHoursTest {
    @Test
    fun `time is within hours`() {
        val operatingHours = OperatingHours.from(openTime, closeTime)
        assertTrue {
            operatingHours.isTimeWithinHours(duringHours)
        }
    }

    @Test
    fun `time is outside working hours`() {
        val operatingHours = OperatingHours.from(openTime, closeTime)
        assertFalse {
            operatingHours.isTimeWithinHours(afterHours)
        }
    }

    @Test
    fun `get open time`() {
        val operatingHours = OperatingHours.from(openTime, closeTime)
        assertEquals(openTime, operatingHours.openTime)
    }

    @Test
    fun `get close time`() {
        val operatingHours = OperatingHours.from(openTime, closeTime)
        assertEquals(closeTime, operatingHours.closeTime)
    }

    @Test
    fun `Two operating Hours are same when open and close time are same`() {
        val operatingHours1 = OperatingHours.from(openTime, closeTime)
        val operatingHours2 = OperatingHours.from(openTime, closeTime)
        assertEquals(operatingHours1, operatingHours2)
    }

    @Test
    fun `Two operating Hours are different when open and close time are not same`() {
        val operatingHours1 = OperatingHours.from(openTime, closeTime)
        val operatingHours2 = OperatingHours.from(openTime, duringHours)
        assertNotEquals(operatingHours1, operatingHours2)
    }

    @Test
    fun `Prints operating hours`() {
        val operatingHours = OperatingHours.from(openTime, closeTime)
        assertEquals(
            "OperatingHours(openTime=${openTime}, closeTime=${closeTime})",
            operatingHours.toString()
        )
    }

    @Test
    fun `Throws exception when invalid open and close time`() {
        val exception = assertThrows<IllegalStateException> {
            OperatingHours.from(closeTime, openTime)
        }
        assertEquals("Open time should be before close time", exception.message)
    }
}