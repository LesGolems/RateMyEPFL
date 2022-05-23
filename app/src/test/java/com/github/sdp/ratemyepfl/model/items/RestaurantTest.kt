package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class RestaurantTest {

    companion object {
        private val EXPECTED_RESTAURANT = Restaurant("Arcadie", 0, 0.0, 0, 46.52, 6.569)
        private val EXPECTED_JSON = Json.encodeToString(EXPECTED_RESTAURANT)
    }

    @Test
    fun defaultConstructorWorks() {
        val r = Restaurant("Arcadie", 0, 0.0, 0, 46.52, 6.569)
        assertEquals("Arcadie", r.name)
    }

    @Test
    fun serializationWorks() {
        val json = Json.encodeToString(EXPECTED_RESTAURANT)
        assertEquals(EXPECTED_JSON, json)
    }

    @Test
    fun deserializationWorks() {
        val r = Json.decodeFromString<Restaurant>(EXPECTED_JSON)
        assertEquals(EXPECTED_RESTAURANT, r)
    }

    @Test
    fun toStringWorks() {
        assertEquals(EXPECTED_RESTAURANT.toString(), "Arcadie")
    }
}