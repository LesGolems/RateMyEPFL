package com.github.sdp.ratemyepfl.model.time

import com.github.sdp.ratemyepfl.model.time.DateTime.Companion.toDateTime
import org.junit.Assert.*
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
}