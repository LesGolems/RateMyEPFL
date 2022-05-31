package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.backend.database.LoaderRepository
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.reviewable.RestaurantRepository
import com.github.sdp.ratemyepfl.backend.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Restaurant
import javax.inject.Inject

class FakeRestaurantRepository @Inject constructor(val repository: FakeLoaderRepository<Restaurant>) :
    RestaurantRepository, ReviewableRepository<Restaurant>,
    LoaderRepository<Restaurant> by repository {

    override val offlineData: List<Restaurant> = RestaurantRepositoryImpl.OFFLINE_RESTAURANTS

    init {
        repository.elements = RESTAURANT_LIST.toSet()
    }

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
            baseRestaurant.copy(
                name = "Roulotte du Soleil",
                occupancy = 0,
                lat = 46.519214,
                long = 6.567553
            ),
            baseRestaurant.copy(name = "Arcadie", occupancy = 0, lat = 46.520625, long = 6.569403),
            baseRestaurant.copy(name = "Takinoa", occupancy = 0, lat = 46.518236, long = 6.568110)
        )

        val DEFAULT_RESTAURANT =
            baseRestaurant.copy(
                name = "Roulotte du Soleil",
                occupancy = 0,
                lat = 46.519214,
                long = 6.567553
            )

        val RESTAURANT_WITH_NO_OCCUPANCY = baseRestaurant.copy(occupancy = 0)
        val RESTAURANT_WITH_MEDIUM_OCCUPANCY = baseRestaurant.copy(occupancy = 20)
        val RESTAURANT_WITH_FULL_OCCUPANCY = baseRestaurant.copy(occupancy = 50)

        val RESTAURANT_NO_REVIEWS = baseRestaurant.copy(grade = 0.0, numReviews = 0)
        val RESTAURANT_WITH_REVIEWS = baseRestaurant.copy(grade = 5.5, numReviews = 1)

        var restaurantById = DEFAULT_RESTAURANT

        var occupancyCounter = 0
    }


    override suspend fun getRestaurants(): List<Restaurant> = RESTAURANT_LIST

    override suspend fun getRestaurantByName(name: String): Restaurant = restaurantById

    override suspend fun incrementOccupancy(id: String) {
        occupancyCounter += 1
    }

    override suspend fun decrementOccupancy(id: String) {
        occupancyCounter -= 1
    }

}