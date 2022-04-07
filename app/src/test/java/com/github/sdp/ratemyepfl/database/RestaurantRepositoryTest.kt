package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.RestaurantRepository.Companion.toRestaurant
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.google.firebase.firestore.DocumentSnapshot
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

class RestaurantRepositoryTest {
    @Test
    fun returnsARestaurantForCompleteSnapshot() {
        val fake = "fake"
        val lat = 0.0
        val long = 0.0

        val snapshot = Mockito.mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(Repository.NUM_REVIEWS_FIELD)).thenReturn("15")
        Mockito.`when`(snapshot.getString(Repository.AVERAGE_GRADE_FIELD)).thenReturn("2.5")
        Mockito.`when`(snapshot.getString("lat")).thenReturn(lat.toString())
        Mockito.`when`(snapshot.getString("long")).thenReturn(long.toString())

        val restaurant = snapshot.toRestaurant()
        val fakeRestaurant = Restaurant.Builder()
            .setId(fake)
            .setNumReviews(15)
            .setAverageGrade(2.5)
            .setLat(lat)
            .setLong(long)
            .build()
        assertEquals(fakeRestaurant, restaurant)

    }

    @Test
    fun toItemReturnsNullForInCompleteSnapshot() {
        val snapshot = Mockito.mock(DocumentSnapshot::class.java)

        Mockito.`when`(snapshot.id).thenReturn(null)
        Mockito.`when`(snapshot.getString(CourseRepository.TITLE_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(CourseRepository.SECTION_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(CourseRepository.TEACHER_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(CourseRepository.CREDITS_FIELD_NAME)).thenReturn(null)
        Mockito.`when`(snapshot.getString(Repository.NUM_REVIEWS_FIELD)).thenReturn(null)
        Mockito.`when`(snapshot.getString(Repository.AVERAGE_GRADE_FIELD)).thenReturn(null)

        val restaurant = snapshot.toRestaurant()
        assertEquals(null, restaurant)
    }
}