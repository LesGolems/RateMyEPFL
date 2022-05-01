package com.github.sdp.ratemyepfl.model.items

import org.junit.Assert.assertEquals
import org.junit.Test

class ClassTest {

    @Test
    fun constructorWithDefaultValues() {
        val c = Class()
        assertEquals(null, c.id)
        assertEquals(null, c.name)
        assertEquals(null, c.teacher)
        assertEquals(null, c.room)
        assertEquals(null, c.day)
        assertEquals(null, c.start)
        assertEquals(null, c.end)
    }


    @Test
    fun constructorWithAllValues() {
        val c = Class(0, "ML", "Jean-Kevin Bite Lavoisier", "INF", 0, 10, 12)
        assertEquals(0, c.id)
        assertEquals("ML", c.name)
        assertEquals("Singe", c.teacher)
        assertEquals("INF", c.room)
        assertEquals(0, c.day)
        assertEquals(10, c.start)
        assertEquals(12, c.end)
    }

    @Test
    fun duration() {
        val c = Class(start = 10, end = 12)
        assertEquals(2, c.duration())
    }

}