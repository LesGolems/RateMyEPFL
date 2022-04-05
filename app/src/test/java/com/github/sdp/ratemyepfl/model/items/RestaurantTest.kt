package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class RestaurantTest {
    val EXPECTED_RESTAURANT = Restaurant("Arcadie")
    val EXPECTED_JSON = Json.encodeToString(EXPECTED_RESTAURANT)

    @Test
    fun defaultConstructorWorks() {
        val r = Restaurant("Arcadie")
        assertEquals("Arcadie", r.id)
    }

    @Test
    fun paramConstructorWorks() {
        val r = Restaurant("Arcadie")
        assertEquals("Arcadie", r.id)
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

    @Test
    fun builderThrowsForMissingId() {
        val builder = Restaurant.Builder()
            .setId(null)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderSucceedsForMissingNonMandatoryProperties() {
        val fake = "fake"
        val builder = Restaurant.Builder()
            .setId(fake)

        val restaurant = Restaurant(fake)
        assertEquals(restaurant, builder.build())
    }
}