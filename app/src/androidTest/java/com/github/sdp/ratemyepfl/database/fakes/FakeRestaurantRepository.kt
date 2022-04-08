package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.RestaurantRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import javax.inject.Inject

class FakeRestaurantRepository @Inject constructor() : RestaurantRepositoryInterface {

    companion object {
        val RESTAURANT_LIST = listOf(
            Restaurant(id = "Roulotte du Soleil", 0, 46.519214, 6.567553, 15, 2.5),
            Restaurant(id = "Arcadie", 0, 46.520625, 6.569403, 8, 3.0),
            Restaurant(id = "Takinoa", 0, 5.0, 6.0, 1, 4.0)
        )

        val DEFAULT_RESTAURANT = Restaurant(id = "Roulotte du Soleil", 0, 1.0, 2.0, 0, 0.0)

        val RESTAURANT_WITH_REVIEWS = Restaurant(id = "Roulotte du Soleil", 0, 1.0, 2.0, 15, 2.5)
        val RESTAURANT_WITHOUT_REVIEWS = Restaurant(id = "Roulotte du Soleil", 0, 1.0, 2.0, 0, 0.0)

        var restaurantById = RESTAURANT_WITH_REVIEWS
    }


    override suspend fun getRestaurants(): List<Restaurant> = RESTAURANT_LIST

    override suspend fun getRestaurantById(id: String): Restaurant = restaurantById

    override suspend fun incrementOccupancy(id: String) {
    }

    override suspend fun decrementOccupancy(id: String) {
    }


    override suspend fun updateRestaurantRating(id: String, rating: ReviewRating) {}
}