package com.github.sdp.ratemyepfl.model.time

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class TimeTest {
    @Test
    fun throwForInvalidHour() {
        assertThrows(IllegalArgumentException::class.java) {
            Time(24, 0)
        }
    }

    @Test
    fun throwForInvalidMin() {
        assertThrows(IllegalArgumentException::class.java) {
            Time(0, -1)
        }
    }

    @Test
    fun beforeTimeIsLess() {
        assertEquals(true, Time(0, 0) < Time(0, 1))
    }

    @Test
    fun afterTimeIsMore() {
        assertEquals(true, Time(12, 12) > Time(11, 50))
    }

    @Test
    fun sameTimeIsZero() {
        assertEquals(false, Time(12, 12) > Time(12, 12))
    }

    @Test
    fun equalityHoldsForSameTime() {
        assertEquals(Time(12, 12), Time(12, 12))
    }

    @Test
    fun toStringHasTheRightFormat() {
        val t = Time(1, 1)
        assertEquals("01h01", t.toString())
    }
}