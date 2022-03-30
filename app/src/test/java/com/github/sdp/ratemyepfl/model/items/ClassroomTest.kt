package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class ClassroomTest {
    val EXPECTED_ROOM = Classroom("CE 1 3", "Auditorium")
    val EXPECTED_JSON = Json.encodeToString(EXPECTED_ROOM)

    @Test
    fun constructorWithDefaultValuesWorks() {
        val r = Classroom("CO1")
        assertEquals("CO1", r.id)
        assertEquals(null, r.roomKind)
    }

    @Test
    fun constructorWithAllValuesWorks() {
        val r = Classroom("CM 1 4","Auditorium")
        assertEquals("CM 1 4", r.id)
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