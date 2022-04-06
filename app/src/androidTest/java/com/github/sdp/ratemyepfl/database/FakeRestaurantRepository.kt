package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Restaurant
import javax.inject.Inject

class FakeRestaurantRepository @Inject constructor() : RestaurantRepositoryInterface {

    companion object {
        val RESTAURANT_LIST = listOf(
            Restaurant(id = "Roulotte du Soleil", 46.519214, 6.567553),
            Restaurant(id = "Arcadie", 46.520625, 6.569403),
            Restaurant(id = "Takinoa", 5.0, 6.0)
        )

        val DEFAULT_RESTAURANT = Restaurant(id = "Roulotte du Soleil", 46.519214, 6.567553)
    }


    override suspend fun getRestaurants(): List<Restaurant> = RESTAURANT_LIST

    override suspend fun getRestaurantById(id: String): Restaurant = DEFAULT_RESTAURANT
}