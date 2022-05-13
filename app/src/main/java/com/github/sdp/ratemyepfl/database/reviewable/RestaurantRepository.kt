package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.model.items.Restaurant

interface RestaurantRepository : ReviewableRepository<Restaurant> {
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

    /**
     *  Increment occupancy of given restaurant
     */
    suspend fun incrementOccupancy(id: String)


    /**
     *  Decrement occupancy of given restaurant
     */
    suspend fun decrementOccupancy(id: String)

}