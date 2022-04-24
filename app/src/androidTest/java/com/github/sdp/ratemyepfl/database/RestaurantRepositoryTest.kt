package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepositoryImpl.Companion.toRestaurant
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.mockito.Mockito
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class RestaurantRepositoryTest {
    private val testRestaurant = Restaurant(
        "Fake id", 1, 0.0,
        0.0,  0, 0.0)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var restaurantRepo: RestaurantRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
        restaurantRepo.add(testRestaurant)
    }

    @After
    fun clean(){
        //restaurantRepo.remove(testRestaurant.id)
    }

    @Test
    fun getRestaurantsWorks() {
        runTest {
            val restaurants = restaurantRepo.getRestaurants()
            assertEquals(restaurants.size, 1)

            val restaurant = restaurants[0]
            assertEquals(testRestaurant.name, restaurant.name)
            assertEquals(testRestaurant.lat, restaurant.lat, 0.1)
            assertEquals(testRestaurant.long, restaurant.long, 0.1)
        }
    }

    @Test
    fun getRestaurantByIdWorks() {
        runTest {
            val restaurant = restaurantRepo.getRestaurantById(testRestaurant.name)
            assertNotNull(restaurant)
            assertEquals(testRestaurant.name, restaurant!!.name)
            assertEquals(testRestaurant.lat, restaurant.lat, 0.1)
            assertEquals(testRestaurant.long, restaurant.long, 0.1)
        }
    }

    @Test
    fun updateRestaurantRatingWorks() {
        runTest {
            restaurantRepo.updateRestaurantRating(testRestaurant.name, ReviewRating.EXCELLENT)
            val restaurant = restaurantRepo.getRestaurantById(testRestaurant.name)
            assertNotNull(restaurant)
            assertEquals(testRestaurant.name, restaurant!!.name)
            assertEquals(1, restaurant.numReviews)
            assertEquals(5.0, restaurant.averageGrade, 0.1)
        }
    }

    @Test
    fun occupancyWorks() {
        runTest {
            restaurantRepo.incrementOccupancy(testRestaurant.name)
            var restaurant = restaurantRepo.getRestaurantById(testRestaurant.name)
            assertNotNull(restaurant)
            assertEquals(testRestaurant.name, restaurant!!.name)
            assertEquals(2, restaurant.occupancy)

            restaurantRepo.decrementOccupancy(testRestaurant.name)
            restaurant = restaurantRepo.getRestaurantById(testRestaurant.name)
            assertNotNull(restaurant)
            assertEquals(testRestaurant.name, restaurant!!.name)
            assertEquals(1, restaurant.occupancy)
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
        Mockito.`when`(snapshot.getString(ReviewableRepositoryImpl.NUM_REVIEWS_FIELD_NAME)).thenReturn("15")
        Mockito.`when`(snapshot.getString(ReviewableRepositoryImpl.AVERAGE_GRADE_FIELD_NAME)).thenReturn("2.5")
        Mockito.`when`(snapshot.getString("lat")).thenReturn(lat.toString())
        Mockito.`when`(snapshot.getString("long")).thenReturn(long.toString())
        Mockito.`when`(snapshot.getString("occupancy")).thenReturn(occupancy.toString())

        val restaurant = snapshot.toRestaurant()
        val fakeRestaurant = Restaurant.Builder()
            .setName(fake)
            .setNumReviews(15)
            .setAverageGrade(2.5)
            .setLat(lat)
            .setLong(long)
            .build()
        assertEquals(fakeRestaurant, restaurant)
    }

}