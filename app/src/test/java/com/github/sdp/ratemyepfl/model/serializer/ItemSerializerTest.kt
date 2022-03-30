package com.github.sdp.ratemyepfl.model.serializer

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.items.Reviewable
import org.junit.Assert.assertEquals
import org.junit.Test

class ItemSerializerTest {
    @Test
    fun serializationTestForClassroom() {
        val t: Reviewable = Classroom("a")
        val ser: String = ItemSerializer.serialize(t)
        val deser: Reviewable? = ItemSerializer.deserialize(ser)
        assertEquals(t, deser)
    }

    @Test
    fun serializationTestForCourse() {
        val t: Reviewable = Course("", "", "", 0, "")
        val ser: String = ItemSerializer.serialize(t)
        val deser: Reviewable? = ItemSerializer.deserialize(ser)
        assertEquals(t, deser)
    }

    @Test
    fun serializationTestForRestaurant() {
        val t: Reviewable = Restaurant("a")
        val ser: String = ItemSerializer.serialize(t)
        val deser: Reviewable? = ItemSerializer.deserialize(ser)
        assertEquals(t, deser)
    }
}