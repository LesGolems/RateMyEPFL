package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class RestaurantTest {
    val EXPECTED_RESTAURANT = Restaurant("Arcadie", 3, 3.5)
    val EXPECTED_JSON = "{\"id\":\"Arcadie\",\"numRatings\":3,\"avgRating\":3.5\"}"

    @Test
    fun defaultConstructorWorks(){
        val r = Restaurant("Arcadie")
        assertEquals("Arcadie", r.id)
        assertEquals(0, r.numRatings)
        assertEquals(0.0, r.avgRating, 0.00001)
    }

    @Test
    fun paramConstructorWorks(){
        val r = Restaurant("Arcadie", 50, 4.5)
        assertEquals("Arcadie", r.id)
        assertEquals(50, r.numRatings)
        assertEquals(4.5, r.avgRating, 0.0001)
    }

    @Test
    fun serializationWorks(){
        val json = Json.encodeToString(EXPECTED_RESTAURANT)
        assertEquals(EXPECTED_RESTAURANT, json)
    }

    @Test
    fun deserializationWorks(){
        val r = Json.decodeFromString<Restaurant>(EXPECTED_JSON)
        assertEquals(EXPECTED_RESTAURANT, r)
    }

    @Test
    fun toStringWorks(){
        assertEquals(EXPECTED_RESTAURANT.toString(), "Arcadie")
    }

}