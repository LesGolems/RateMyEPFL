package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.RestaurantRepository.Companion.toRestaurant
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.mockito.Mockito
import javax.inject.Inject

@HiltAndroidTest
class RestaurantRepositoryTest {
    private val testRestaurant = Restaurant(
        "Fake id", 0, 0.0,
        0.0,  0, 0.0)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var restaurantRepo: RestaurantRepository

    @Before
    fun setup() {
        hiltRule.inject()
        restaurantRepo.add(testRestaurant)
    }

    @After
    fun clean(){
        restaurantRepo.remove(testRestaurant.id)
    }

    @Test
    fun getRestaurantsWorks() {
        runTest {
            val restaurants = restaurantRepo.getRestaurants()
            assertEquals(restaurants.size, 1)

            val restaurant = restaurants[0]
            assertEquals(restaurant.id, testRestaurant.id)
            assertEquals(restaurant.lat, testRestaurant.lat, 0.1)
            assertEquals(restaurant.long, testRestaurant.long, 0.1)
            assertEquals(restaurant.occupancy, testRestaurant.occupancy)
            assertEquals(restaurant.numReviews, restaurant.numReviews)
            assertEquals(restaurant.averageGrade, restaurant.averageGrade, 0.1)
        }
    }

    @Test
    fun getRestaurantByIdWorks() {
        runTest {
            val restaurant = restaurantRepo.getRestaurantById(testRestaurant.id)
            assertNotNull(restaurant)
            assertEquals(restaurant!!.id, testRestaurant.id)
            assertEquals(restaurant.lat, testRestaurant.lat, 0.1)
            assertEquals(restaurant.long, testRestaurant.long, 0.1)
            assertEquals(restaurant.occupancy, testRestaurant.occupancy)
            assertEquals(restaurant.numReviews, restaurant.numReviews)
            assertEquals(restaurant.averageGrade, restaurant.averageGrade, 0.1)
        }
    }

    @Test
    fun updateRestaurantRatingWorks() {
        runTest {
            restaurantRepo.updateRestaurantRating(testRestaurant.id, ReviewRating.EXCELLENT).await()
            val restaurant = restaurantRepo.getRestaurantById(testRestaurant.id)
            assertNotNull(restaurant)
            assertEquals(restaurant!!.id, testRestaurant.id)
            assertEquals(restaurant.lat, testRestaurant.lat, 0.1)
            assertEquals(restaurant.long, testRestaurant.long, 0.1)
            assertEquals(restaurant.occupancy, testRestaurant.occupancy)
            assertEquals(restaurant.numReviews, restaurant.numReviews)
            assertEquals(restaurant.averageGrade, restaurant.averageGrade, 0.1)
        }
    }

    @Test
    fun returnsARestaurantForCompleteSnapshot() {
        val fake = "fake"
        val lat = 0.0
        val long = 0.0
        val occupancy = 0

        val snapshot = Mockito.mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(Repository.NUM_REVIEWS_FIELD)).thenReturn("15")
        Mockito.`when`(snapshot.getString(Repository.AVERAGE_GRADE_FIELD)).thenReturn("2.5")
        Mockito.`when`(snapshot.getString("lat")).thenReturn(lat.toString())
        Mockito.`when`(snapshot.getString("long")).thenReturn(long.toString())
        Mockito.`when`(snapshot.getString("occupancy")).thenReturn(occupancy.toString())

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

}