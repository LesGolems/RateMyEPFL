package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class RestaurantTest {
    val EXPECTED_RESTAURANT = Restaurant("Arcadie", 0, 46.52, 6.569)
    val EXPECTED_JSON = Json.encodeToString(EXPECTED_RESTAURANT)

    @Test
    fun defaultConstructorWorks() {
        val r = Restaurant("Arcadie", 0, 46.52, 6.569)
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

    @Test
    fun builderThrowsForMissingId() {
        val builder = Restaurant.Builder()
            .setName(null)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderSucceedsForMissingNonMandatoryProperties() {
        val fake = "fake"
        val lat = 0.0
        val long = 0.0
        val builder = Restaurant.Builder()
            .setName(fake)
            .setLat(lat)
            .setLong(long)

        val restaurant = Restaurant(fake, 0, lat, long)
        assertEquals(restaurant, builder.build())
    }
}