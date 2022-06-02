package com.github.sdp.ratemyepfl.model.time

import com.github.sdp.ratemyepfl.model.time.Date.Companion.toDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDate
import java.time.Month

class DateTest {
    @Test
    fun nowProvidesTheCurrentDate() {
        val d = Date.now()
        val l = LocalDate.now()
        assertEquals(l.year, d.year)
        assertEquals(l.month, d.month)
    }

    @Test
    fun toStringGivesTheCorrectFormat() {
        val d = Date(2001, 1, 1)
        assertEquals("2001-01-01", d.toString())
    }

    @Test
    fun toDateConvertsCorrectlyALocalDate() {
        val l = LocalDate.of(2001, 1, 1)
        val d = Date(2001, 1, 1)
        assertEquals(d, l.toDate())
    }

    @Test
    fun throwsForInvalidDayOfMonth() {
        assertThrows(IllegalArgumentException::class.java) {
            Date(2000, 1, 32)
        }
    }

    @Test
    fun throwsForThe29FebInNonLeapYear() {
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            Date(2022, Month.FEBRUARY, 29)
        }
    }

    @Test
    fun doesNotThrowsForThe29FebInLeapYear() {
        Date(2024, Month.FEBRUARY, 29)
    }

    @Test
    fun previousDateIsLess() {
        assertEquals(true, Date(2022, 1, 1) < Date(2023, 12, 23))
    }

    @Test
    fun nextDateIsMore() {
        assertEquals(true, Date(2022, 1, 2) > Date(2022, 1, 1))
    }

    @Test
    fun equalityHoldsForSameDate() {
        assertEquals(Date(2022, 1, 1), Date(2022, 1, 1))
    }

    @Test
    fun plusWorks() {
        val d1 = Date(2024, Month.FEBRUARY, 15)
        assertEquals(Date(2024, Month.FEBRUARY, 29), d1.plus(14))
    }

    @Test
    fun toLocalDateWorks() {
        val d1 = Date(2024, Month.FEBRUARY, 15)
        assertEquals(LocalDate.of(2024, Month.FEBRUARY, 15), d1.toLocalDate())
    }

    @Test
    fun compareToWorks() {
        val d1 = Date(2024, Month.FEBRUARY, 15)
        val d2 = Date(2023, Month.FEBRUARY, 15)
        val d3 = Date(2024, Month.JANUARY, 15)
        val d4 = Date(2024, Month.FEBRUARY, 14)
        assertEquals(false, d1 < d1)
        assertEquals(true, d1 > d2)
        assertEquals(true, d1 > d3)
        assertEquals(true, d1 > d4)
    }
}