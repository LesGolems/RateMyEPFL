package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class ClassroomTest {
    val EXPECTED_ROOM = Classroom("CE 1 3", 3, 4.9, "Auditorium")
    val EXPECTED_JSON = "{\"id\":\"CE 1 3\",\"numRatings\":3,\"avgRating\":4.9,\"type\":\"Auditorium\"}"

    @Test
    fun constructorWithDefaultValuesWorks(){
        val r = Classroom("CO1")
        assertEquals("CO1", r.id)
        assertEquals(0, r.numRatings)
        assertEquals(0.0, r.avgRating, 0.00001)
        assertEquals(null, r.type)
    }

    @Test
    fun constructorWithAllValuesWorks(){
        val r = Classroom("CM 1 4", 89, 2.35, "Auditorium")
        assertEquals("CM 1 4", r.id)
        assertEquals(89, r.numRatings)
        assertEquals(2.35, r.avgRating, 0.0001)
        assertEquals("Auditorium", r.type)
    }

    @Test
    fun serializationWorks(){
        val json = Json.encodeToString(EXPECTED_ROOM)
        assertEquals(EXPECTED_JSON, json)
    }

    @Test
    fun deserializationWorks(){
        val course = Json.decodeFromString<Classroom>(EXPECTED_JSON)
        assertEquals(EXPECTED_ROOM, course)
    }
}