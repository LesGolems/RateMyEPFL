package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ClassroomTest {
    val EXPECTED_ROOM = Classroom("CE 1 3", "Auditorium")
    val EXPECTED_JSON = Json.encodeToString(EXPECTED_ROOM)

    @Test
    fun constructorWithDefaultValuesWorks() {
        val r = Classroom("CO1")
        assertEquals("CO1", r.name)
        assertEquals(null, r.roomKind)
    }

    @Test
    fun constructorWithAllValuesWorks() {
        val r = Classroom("CM 1 4", "Auditorium")
        assertEquals("CM 1 4", r.name)
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

    @Test
    fun builderThrowsForMissingId() {
        val fake = "Fake"
        val builder = Classroom.Builder()
            .setRoomKind(fake)
            .setName(null)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderSucceedsForMissingNonMandatoryProperties() {
        val fake = "Fake"
        val builder = Classroom.Builder()
            .setName(fake)

        val classroom = Classroom(fake)
        assertEquals(classroom, builder.build())
    }
}