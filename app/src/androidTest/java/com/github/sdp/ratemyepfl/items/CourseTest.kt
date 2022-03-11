package com.github.sdp.ratemyepfl.items

import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CourseTest {

    @Test
    fun constructorWorks(){
        val c = Course("SDP", "IC", "Candea", 4, "CS-306")
        assertEquals(c.name, "SDP")
        assertEquals(c.faculty, "IC")
        assertEquals(c.teacher, "Candea")
        assertEquals(c.credits, 4)
        assertEquals(c.courseCode, "CS-306")
    }

    @Test
    fun toStringWorks(){
        val c = Course("SDP", "IC", "Candea", 4, "CS-306")
        assertEquals(c.toString(), "CS-306 SDP")
    }

}