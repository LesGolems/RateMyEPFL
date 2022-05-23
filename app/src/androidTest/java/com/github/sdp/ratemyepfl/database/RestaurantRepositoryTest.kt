package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepositoryImpl.Companion.toRestaurant
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class RestaurantRepositoryTest {
    private val testRestaurant = Restaurant(
        "Fake id", 1, 2.5, 1, 0.0,
        0.0
    )

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
    fun clean() = runTest {
        restaurantRepo.remove(testRestaurant.getId()).await()
    }

    @Test
    fun conversionTest() = runTest {
        restaurantRepo.add(testRestaurant).await()
        val c = restaurantRepo.getRestaurantById(testRestaurant.getId())
        assertEquals(testRestaurant, c)
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
            assertEquals(testRestaurant.grade, restaurant.grade, 0.1)
            assertEquals(testRestaurant.numReviews, restaurant.numReviews)
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
            assertEquals(testRestaurant.grade, restaurant.grade, 0.1)
            assertEquals(testRestaurant.numReviews, restaurant.numReviews)
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
}