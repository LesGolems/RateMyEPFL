package com.github.sdp.ratemyepfl.model.time

import org.junit.Assert.*
import org.junit.Test

class PeriodTest {
    @Test
    fun periodSpansOnIncreasingTime() {
        val start = DateTime(2022, 1, 1, 0, 0)
        val end = DateTime(2021, 1, 1, 0, 0)
        assertThrows(IllegalArgumentException::class.java) {
            Period(start, end)
        }
    }
}