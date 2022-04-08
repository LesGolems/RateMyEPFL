package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task

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

    /**
     *  Increment occupancy of given restaurant
     */
    fun incrementOccupancy(id: String)


    /**
     *  Decrement occupancy of given restaurant
     */
    fun decrementOccupancy(id: String)


    /**
     *  Updates the rating of the restaurant using a transaction for concurrency
     *
     *  @param id : id of the reviewed item
     *  @param rating: rating of the review being added
     */
    fun updateRestaurantRating(id: String, rating: ReviewRating): Task<Unit>
}