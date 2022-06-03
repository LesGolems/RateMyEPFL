package com.github.sdp.ratemyepfl.model.time

import com.github.sdp.ratemyepfl.model.time.DateTime.Companion.toDateTime
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class DateTimeTest {
    @Test
    fun addDurationWithOverflowWorks() {
        val date = LocalDateTime.of(2021, 12, 31, 12, 0).toDateTime()
        val duration = Duration(3, 13, 30)
        val (computedDate, computedTime) = date.plus(duration)
        assertEquals(computedDate.year, 2022)
        assertEquals(computedDate.month.value, 1)
        assertEquals(computedDate.dayOfMonth, 4)
        assertEquals(computedTime.hours, 1)
        assertEquals(computedTime.minutes, 30)
    }

    @Test
    fun conversionWorks() {
        val expected = LocalDateTime.of(2022, 1, 1, 0, 0)
        val d = DateTime(2022, 1, 1, 0, 0)
        assertEquals(expected, d.toLocalDateTime())

        assertEquals(d, expected.toDateTime())
    }

    @Test
    fun beforeIsLess() {
        val d1 = DateTime(2022, 1, 1, 12, 0)
        val d2 = DateTime(2022, 1, 1, 0, 56)
        assertEquals(true, d2 < d1)
    }

    @Test
    fun afterIsMore() {
        val d1 = DateTime(2022, 1, 3, 12, 0)
        val d2 = DateTime(2022, 1, 1, 0, 56)
        assertEquals(true, d1 > d2)
    }

    @Test
    fun sameTimeIsEqual() {
        val d1 = DateTime(2022, 1, 1, 0, 0)
        val d2 = d1.copy()
        assertEquals(true, d1 == d2)
    }
}