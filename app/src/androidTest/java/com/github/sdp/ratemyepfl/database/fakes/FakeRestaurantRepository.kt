package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.LoaderRepository
import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepository
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeRestaurantRepository @Inject constructor(val repository: FakeLoaderRepository<Restaurant>) :
    RestaurantRepository, ReviewableRepository<Restaurant>,  LoaderRepository<Restaurant> by repository {

    override val offlineData: List<Restaurant> = RestaurantRepositoryImpl.OFFLINE_RESTAURANTS

    companion object {
        private val baseRestaurant = Restaurant(
            name = "Roulotte du Soleil",
            occupancy = 0,
            lat = 0.0,
            long = 1.0,
            grade = 2.0,
            numReviews = 1
        )
        val RESTAURANT_LIST = listOf(
            baseRestaurant.copy(name = "Roulotte du Soleil", occupancy =  0, lat =  46.519214, long = 6.567553),
            baseRestaurant.copy(name = "Arcadie", occupancy =  0, lat =  46.520625, long = 6.569403),
            baseRestaurant.copy(name = "Takinoa", occupancy =  0, lat =  46.518236, long = 6.568110)
        )

        val DEFAULT_RESTAURANT =
            baseRestaurant.copy(name = "Roulotte du Soleil", occupancy = 0, lat = 46.519214, long = 6.567553)

        val RESTAURANT_WITH_NO_OCCUPANCY = baseRestaurant.copy(occupancy = 0)
        val RESTAURANT_WITH_MEDIUM_OCCUPANCY = baseRestaurant.copy(occupancy = 20)
        val RESTAURANT_WITH_FULL_OCCUPANCY = baseRestaurant.copy(occupancy = 50)

        val RESTAURANT_NO_REVIEWS = baseRestaurant.copy(grade = 0.0, numReviews = 0)
        val RESTAURANT_WITH_REVIEWS = baseRestaurant.copy(grade = 5.5, numReviews = 1)

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

}