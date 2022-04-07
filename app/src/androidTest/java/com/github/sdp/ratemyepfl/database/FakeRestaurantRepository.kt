package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import javax.inject.Inject

class FakeRestaurantRepository @Inject constructor() : RestaurantRepositoryInterface {

    companion object {
        val RESTAURANT_LIST = listOf(
            Restaurant(id = "Roulotte du Soleil", 15, 2.5, 46.519214, 6.567553),
            Restaurant(id = "Arcadie", 10, 3.0, 46.520625, 6.569403),
            Restaurant(id = "Takinoa", 1, 4.0,  5.0, 6.0)
        )

        val RESTAURANT_WITH_REVIEWS = Restaurant(id = "Roulotte du Soleil", 15, 2.5, 1.0, 2.0)
        val RESTAURANT_WITHOUT_REVIEWS = Restaurant(id = "Roulotte du Soleil", 0, 0.0, 1.0, 2.0)

        var restaurantById = RESTAURANT_WITH_REVIEWS
    }


    override suspend fun getRestaurants(): List<Restaurant> = RESTAURANT_LIST

    override suspend fun getRestaurantById(id: String): Restaurant = restaurantById
    override fun updateRestaurantRating(id: String, rating: ReviewRating) {

    }
}