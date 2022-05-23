package com.github.sdp.ratemyepfl.model.time

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Month

class DayOfMonthTest {
    @Test
    fun falseFor29FebInNonLeapYear() {
        assertEquals(false, DayOfMonth.isValid(29, Month.FEBRUARY, 2022))
    }

    @Test
    fun trueForThe29FebInLeapYear() {
        assertEquals(
            true,
            DayOfMonth.isValid(29, Month.FEBRUARY, 2024)
        )
    }
}