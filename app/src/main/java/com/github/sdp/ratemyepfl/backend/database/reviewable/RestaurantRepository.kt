package com.github.sdp.ratemyepfl.backend.database.reviewable

import com.github.sdp.ratemyepfl.model.items.Restaurant

interface RestaurantRepository : ReviewableRepository<Restaurant> {
    /**
     * Retrieve the restaurants from the repository
     *
     * @return a list of non-null restaurant
     */
    suspend fun getRestaurants(): List<Restaurant>

    /**
     * Retrieve an restaurant from id.
     *
     * @return the restaurant if found, otherwise null
     *
     * @throws NoSuchElementException if no element with the provided id exists
     */
    suspend fun getRestaurantByName(name: String): Restaurant

    /**
     *  Increment occupancy of given restaurant
     */
    suspend fun incrementOccupancy(id: String)


    /**
     *  Decrement occupancy of given restaurant
     */
    suspend fun decrementOccupancy(id: String)

}