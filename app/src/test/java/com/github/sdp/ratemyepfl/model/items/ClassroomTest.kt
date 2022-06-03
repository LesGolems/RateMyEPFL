package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class ClassroomTest {

    companion object {
        private val EXPECTED_ROOM = Classroom("CE 1 3", 0.0, 0, "Auditorium")
        private val EXPECTED_JSON = Json.encodeToString(EXPECTED_ROOM)
    }

    @Test
    fun constructorWithDefaultValuesWorks() {
        val g = 0.0
        val n = 0
        val r = Classroom("CO1", g, n)
        assertEquals("CO1", r.name)
        assertEquals(g, r.grade, 0.1)
        assertEquals(n, r.numReviews)
        assertEquals(null, r.roomKind)
    }

    @Test
    fun constructorWithAllValuesWorks() {
        val g = 0.0
        val n = 0
        val r = Classroom("CM 1 4", g, n, "Auditorium")
        assertEquals("CM 1 4", r.name)
        assertEquals(g, r.grade, 0.01)
        assertEquals(n, r.numReviews)
        assertEquals("Auditorium", r.roomKind)
    }

    @Test
    fun serializationWorks() {
        val json = Json.encodeToString(EXPECTED_ROOM)
        assertEquals(EXPECTED_JSON, json)
    }

    @Test
    fun deserializationWorks() {
        val course = Json.decodeFromString<Classroom>(EXPECTED_JSON)
        assertEquals(EXPECTED_ROOM, course)
    }

}