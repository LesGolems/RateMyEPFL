package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.ClassroomRepository.Companion.toClassroom
import com.github.sdp.ratemyepfl.database.RestaurantRepository.Companion.toRestaurant
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.google.firebase.firestore.DocumentSnapshot
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class RestaurantRepositoryTest {
    @Test
    fun returnsARestaurantForCompleteSnapshot() {
        val fake = "fake"

        val snapshot = Mockito.mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id).thenReturn(fake)

        val restaurant = snapshot.toRestaurant()
        val fakeRestaurant = Restaurant.Builder()
            .setId(fake)
            .build()
        assertEquals(fakeRestaurant, restaurant)

    }

    @Test
    fun toItemReturnsNullForInCompleteSnapshot() {
        val fake = "fake"
        val snapshot = Mockito.mock(DocumentSnapshot::class.java)

        Mockito.`when`(snapshot.id).thenReturn(null)
        Mockito.`when`(snapshot.getString(CourseRepository.TITLE_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(CourseRepository.SECTION_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(CourseRepository.TEACHER_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(CourseRepository.CREDITS_FIELD_NAME)).thenReturn(null)

        val restaurant = snapshot.toRestaurant()
        assertEquals(null, restaurant)
    }
}