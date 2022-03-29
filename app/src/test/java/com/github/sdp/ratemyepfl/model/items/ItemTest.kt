package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class ItemTest {
    @Test
    fun serializationTest() {
        val t: Reviewable = Classroom("a")
        val ser: String = Json.encodeToString(t)
        val deser: Reviewable = Json.decodeFromString<Reviewable>(ser)
    }
}