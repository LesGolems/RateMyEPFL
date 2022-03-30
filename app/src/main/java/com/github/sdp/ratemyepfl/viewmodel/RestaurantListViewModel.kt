package com.github.sdp.ratemyepfl.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import com.github.sdp.ratemyepfl.database.ItemRepository
import com.github.sdp.ratemyepfl.model.items.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(repository: ItemRepository<Restaurant>) :
    ReviewableListViewModel<Restaurant>(repository) {

    private val precision: Double = 0.5

    private val nearbyRestaurants: List<Restaurant> = listOf()
    private val insideRestaurant: Restaurant? = null

    fun getRestaurants(): LiveData<List<Restaurant>> {
        return getItemsAsLiveData()
    }

    fun getRestaurantByLocation(location: Location): LiveData<List<Restaurant>> {
        TODO()
    }

    suspend fun updateRestaurantsOccupancy(location: Location) {
        getItems().map { restaurant ->
            if (restaurant.latitude - location.latitude < precision && restaurant.longitude - location.longitude < precision) {
                // add one occupancy
            }
        }
    }

}
