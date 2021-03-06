package com.github.sdp.ratemyepfl.model.items

import org.junit.Assert.assertEquals
import org.junit.Test

class ClassTest {

    @Test
    fun constructorWithDefaultValues() {
        val c = Class()
        assertEquals(null, c.name)
        assertEquals(null, c.teacher)
        assertEquals(null, c.room)
        assertEquals(null, c.day)
        assertEquals(null, c.start)
        assertEquals(null, c.end)
    }


    @Test
    fun constructorWithAllValues() {
        val c = Class("CS", "ML", "Jean-Kevin Bite Lavoisier", "INF", 0, 10, 12)
        assertEquals("ML", c.name)
        assertEquals("Jean-Kevin Bite Lavoisier", c.teacher)
        assertEquals("INF", c.room)
        assertEquals(0, c.day)
        assertEquals(10, c.start)
        assertEquals(12, c.end)
    }

    @Test
    fun describeContentsReturnsZero() {
        val c = Class("CS", "ML", "Jean-Kevin Bite Lavoisier", "INF", 0, 10, 12)
        assertEquals(c.describeContents(), 0)
    }

    @Test
    fun newArrayHasTheCorrectSize() {
        val l = Class.newArray(5)
        assertEquals(l.size, 5)
    }

    @Test
    fun duration() {
        val c = Class(start = 10, end = 12)
        assertEquals(2, c.duration())
    }

}