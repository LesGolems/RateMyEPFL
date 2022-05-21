package com.github.sdp.ratemyepfl.backend.database.firebase.reviewable

import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.RestaurantRepositoryImpl.Companion.toRestaurant
import com.github.sdp.ratemyepfl.backend.database.reviewable.ReviewableRepository
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

    @Test
    fun returnsARestaurantForCompleteSnapshot() {
        val fake = "fake"
        val lat = 0.0
        val long = 0.0
        val occupancy = 0
        val grade = 0.0
        val n = 0

        val snapshot = Mockito.mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id).thenReturn(fake)
        Mockito.`when`(snapshot.getString(RestaurantRepositoryImpl.RESTAURANT_NAME_FIELD_NAME))
            .thenReturn(fake)
        Mockito.`when`(
            snapshot.getField<Int>(
                ReviewableRepository.NUM_REVIEWS_FIELD_NAME
            )
        ).thenReturn(n)
        Mockito.`when`(snapshot.getDouble(ReviewableRepository.AVERAGE_GRADE_FIELD_NAME))
            .thenReturn(grade)
        Mockito.`when`(snapshot.getDouble("lat")).thenReturn(lat)
        Mockito.`when`(snapshot.getDouble("long")).thenReturn(long)
        Mockito.`when`(snapshot.getField<Int>("occupancy")).thenReturn(occupancy)

        val restaurant = snapshot.toRestaurant()
        val fakeRestaurant = Restaurant.Builder()
            .setName(fake)
            .setLat(lat)
            .setLong(long)
            .setOccupancy(0)
            .setGrade(grade)
            .setNumReviews(n)
            .build()
        assertEquals(fakeRestaurant, restaurant)
    }

}