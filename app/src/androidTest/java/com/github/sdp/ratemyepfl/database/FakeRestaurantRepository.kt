package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import javax.inject.Inject

class FakeRestaurantRepository @Inject constructor() : RestaurantRepositoryInterface {

    companion object {
        val RESTAURANT_LIST = listOf(
            Restaurant(id = "Roulotte du Soleil", 0, 1.0, 2.0, 0, 0.0),
            Restaurant(id = "Arcadie", 0, 3.0, 4.0, 0, 0.0),
            Restaurant(id = "Takinoa", 0, 5.0, 6.0, 0, 0.0),
            Restaurant(id = "Roulotte du Soleil", 0, 1.0, 2.0, 15, 2.5),
            Restaurant(id = "Arcadie", 0, 3.0, 4.0, 15, 2.5),
            Restaurant(id = "Takinoa", 0, 5.0, 6.0, 15, 2.5)
        )

        val DEFAULT_RESTAURANT = Restaurant(id = "Roulotte du Soleil", 0, 1.0, 2.0, 0, 0.0)

        val RESTAURANT_WITH_REVIEWS = Restaurant(id = "Roulotte du Soleil", 0, 1.0, 2.0, 15, 2.5)
        val RESTAURANT_WITHOUT_REVIEWS = Restaurant(id = "Roulotte du Soleil", 0, 1.0, 2.0, 0, 0.0)

        var restaurantById = RESTAURANT_WITH_REVIEWS
    }


    override suspend fun getRestaurants(): List<Restaurant> = RESTAURANT_LIST

    override suspend fun getRestaurantById(id: String): Restaurant = DEFAULT_RESTAURANT

    override fun incrementOccupancy(id: String) {
    }

    override fun decrementOccupancy(id: String) {
    }


    override fun updateRestaurantRating(id: String, rating: ReviewRating) {

    }
}