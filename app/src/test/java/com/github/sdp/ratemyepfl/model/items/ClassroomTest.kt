package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ClassroomTest {
    val EXPECTED_ROOM = Classroom("CE 1 3", 15, 2.5, "Auditorium")
    val EXPECTED_JSON = Json.encodeToString(EXPECTED_ROOM)

    @Test
    fun constructorWithDefaultValuesWorks() {
        val r = Classroom("CO1", 15, 2.5)
        assertEquals("CO1", r.name)
        assertEquals(15, r.numReviews)
        assertEquals(2.5, r.averageGrade, 0.01)
        assertEquals(null, r.roomKind)
    }

    @Test
    fun constructorWithAllValuesWorks() {
        val r = Classroom("CM 1 4", 15, 2.5, "Auditorium")
        assertEquals("CM 1 4", r.name)
        assertEquals(15, r.numReviews)
        assertEquals(2.5, r.averageGrade, 0.01)
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
            .setNumReviews(15)
            .setAverageGrade(2.5)
            .setRoomKind(fake)
            .setName(null)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsForMissingNumReviews() {
        val fake = "Fake"
        val builder = Classroom.Builder()
            .setAverageGrade(2.5)
            .setRoomKind(fake)
            .setName(fake)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsForMissingAverageGrade() {
        val fake = "Fake"
        val builder = Classroom.Builder()
            .setNumReviews(15)
            .setRoomKind(fake)
            .setName(fake)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderSucceedsForMissingNonMandatoryProperties() {
        val fake = "Fake"
        val builder = Classroom.Builder()
            .setName(fake)
            .setNumReviews(15)
            .setAverageGrade(2.5)

        val classroom = Classroom(fake, 15, 2.5)
        assertEquals(classroom, builder.build())
    }
}