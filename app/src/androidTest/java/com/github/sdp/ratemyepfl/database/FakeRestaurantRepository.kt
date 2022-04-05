package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Restaurant
import javax.inject.Inject

class FakeRestaurantRepository @Inject constructor() : RestaurantRepositoryInterface {

    companion object {
        val RESTAURANT_LIST = listOf(
            Restaurant(id = "Roulotte du Soleil"),
            Restaurant(id = "Arcadie"),
            Restaurant(id = "Takinoa")
        )

        val DEFAULT_RESTAURANT = Restaurant(id = "Roulotte du Soleil")
    }


    override suspend fun getRestaurants(): List<Restaurant> = RESTAURANT_LIST

    override suspend fun getRestaurantById(id: String): Restaurant = DEFAULT_RESTAURANT
}