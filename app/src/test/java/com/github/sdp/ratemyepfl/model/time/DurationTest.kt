package com.github.sdp.ratemyepfl.model.time

import org.junit.Assert.*
import org.junit.Test
import java.lang.IllegalArgumentException

class DurationTest {

    @Test
    fun requiresValidHours() {
        assertThrows(IllegalArgumentException::class.java) {
            Duration(0, -1, 0)
        }

        assertThrows(IllegalArgumentException::class.java) {
            Duration(0, 24, 0)
        }
    }

    @Test
    fun requiresValidMinutes() {
        assertThrows(IllegalArgumentException::class.java) {
            Duration(0, 0, -1)
        }

        assertThrows(IllegalArgumentException::class.java) {
            Duration(0, 0, 60)
        }
    }

    @Test
    fun requireValidDays() {
        assertThrows(IllegalArgumentException::class.java) {
            Duration(-1, 0, 0)
        }
    }

    @Test
    fun addTwoNonConflictingDurationsWorks() {
        val d1 = Duration(1, 3, 32)
        val d2 = Duration(1, 7, 8)
        assertEquals(Duration(2, 10, 40), d1 + d2)
    }

    @Test
    fun addDurationWorksForConflictingDurations() {
        val d1 = Duration(1, 3, 32)
        val d2 = Duration(1, 20, 48)
        assertEquals(Duration(3, 0, 20), d1 + d2)
    }
}