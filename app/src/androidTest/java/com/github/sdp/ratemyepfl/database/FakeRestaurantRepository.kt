package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import javax.inject.Inject

class FakeRestaurantRepository @Inject constructor() : RestaurantRepositoryInterface {

    companion object {
        val RESTAURANT_LIST = listOf(
            Restaurant(id = "Roulotte du Soleil", 15, 2.5, 1.0, 2.0),
            Restaurant(id = "Arcadie", 15, 2.5, 3.0, 4.0),
            Restaurant(id = "Takinoa", 15, 2.5, 5.0, 6.0)
        )

        val DEFAULT_RESTAURANT = Restaurant(id = "Roulotte du Soleil", 15, 2.5, 1.0, 2.0)
    }


    override suspend fun getRestaurants(): List<Restaurant> = RESTAURANT_LIST

    override suspend fun getRestaurantById(id: String): Restaurant = DEFAULT_RESTAURANT
    override fun updateRestaurantRating(id: String, rating: ReviewRating) {

    }
}