package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepository
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeRestaurantRepository @Inject constructor() : RestaurantRepository {

    companion object {
        val RESTAURANT_LIST = listOf(
            Restaurant("Roulotte du Soleil", 0, 46.519214, 6.567553),
            Restaurant("Arcadie", 0, 46.520625, 6.569403),
            Restaurant("Takinoa", 0, 46.518236, 6.568110)
        )

        val DEFAULT_RESTAURANT = Restaurant(name = "Roulotte du Soleil", 0, 1.0, 2.0)

        val RESTAURANT_WITH_NO_OCCUPANCY = Restaurant("Roulotte du Soleil", 0, 1.0, 2.0)
        val RESTAURANT_WITH_MEDIUM_OCCUPANCY =
            Restaurant("Roulotte du Soleil", 20, 1.0, 2.0)
        val RESTAURANT_WITH_FULL_OCCUPANCY = Restaurant("Roulotte du Soleil", 50, 1.0, 2.0)

        var restaurantById = DEFAULT_RESTAURANT

        var occupancyCounter = 0
    }


    override suspend fun getRestaurants(): List<Restaurant> = RESTAURANT_LIST

    override suspend fun getRestaurantById(id: String): Restaurant = restaurantById

    override suspend fun incrementOccupancy(id: String) {
        occupancyCounter += 1
    }

    override suspend fun decrementOccupancy(id: String) {
        occupancyCounter -= 1
    }

    override fun search(prefix: String): QueryResult<List<Restaurant>> = QueryResult(
        flow { emit(QueryState.success(listOf())) }
    )
}