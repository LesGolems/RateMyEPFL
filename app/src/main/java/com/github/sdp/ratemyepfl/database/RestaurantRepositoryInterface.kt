package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Restaurant

interface RestaurantRepositoryInterface {
    /**
     * Retrieve the restaurant from the repository
     *
     * @return a list of non-null restaurant
     */
    suspend fun getRestaurants(): List<Restaurant>

    /**
     * Retrieve an restaurant from id.
     *
     * @return the restaurant if found, otherwise null
     */
    suspend fun getRestaurantById(id: String): Restaurant?
}