package com.github.sdp.ratemyepfl.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.ItemRepository
import com.github.sdp.ratemyepfl.database.RestaurantRepository
import com.github.sdp.ratemyepfl.model.items.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(repository: RestaurantRepository) :
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

    fun updateRestaurantsOccupancy(l: Location) {
        // this there is only one close restaurant

        viewModelScope.launch {
            val id = findCloseRestaurantID(l) ?: return@launch
            //update occupancy

        }
    }

    private suspend fun findCloseRestaurantID(l: Location): String? {
        repository.getItems().map { r ->
            if (isClose(l.latitude, r.latitude, l.longitude, r.longitude)) {
                return r.id
            }
        }
        return null
    }

    private fun isClose(lat1: Double, lat2: Double, long1: Double, long2: Double): Boolean {
        return (lat1 - lat2 < precision) && (long1 - long2 < precision)
    }

}
