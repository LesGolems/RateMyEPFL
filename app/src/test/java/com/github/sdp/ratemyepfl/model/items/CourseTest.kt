package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class CourseTest {

    val EXPECTED_COURSE = Course("SDP", "IC", "Candea", 4, "CS-306")
    val EXPECTED_JSON =
        "{\"title\":\"SDP\",\"section\":\"IC\",\"teacher\":\"Candea\",\"credits\":4,\"id\":\"CS-306\"}"

    @Test
    fun constructorWithDefaultValuesWorks() {
        val c = Course("SDP", "IC", "Candea", 4, "CS-306")
        assertEquals("SDP", c.title)
        assertEquals("IC", c.section)
        assertEquals("Candea", c.teacher)
        assertEquals(4, c.credits)
        assertEquals("CS-306", c.id)
        assertEquals(0, c.numRatings)
        assertEquals(0.0, c.avgRating, 0.00001)
        assertEquals(null, c.cycle)
    }

    @Test
    fun constructorWithAllValuesWorks() {
        val c = Course(
            "SDP", "IC", "Candea", 4, "CS-306", 5,
            3.7, "bachelor", "Fall", "During the semester", "ENG"
        )
        assertEquals("SDP", c.title)
        assertEquals("IC", c.section)
        assertEquals("Candea", c.teacher)
        assertEquals(4, c.credits)
        assertEquals("CS-306", c.id)
        assertEquals(5, c.numRatings)
        assertEquals(3.7, c.avgRating, 0.00001)
        assertEquals("bachelor", c.cycle)
        assertEquals("Fall", c.session)
        assertEquals("During the semester", c.grading)
        assertEquals("ENG", c.language)
    }

    @Test
    fun toStringWorks() {
        assertEquals(EXPECTED_COURSE.toString(), "CS-306 SDP")
    }

    @Test
    fun serializationWorks() {
        val json = Json.encodeToString(EXPECTED_COURSE)
        assertEquals(EXPECTED_JSON, json)
    }

    @Test
    fun deserializationWorks() {
        val course = Json.decodeFromString<Course>(EXPECTED_JSON)
        assertEquals(EXPECTED_COURSE, course)
    }
}