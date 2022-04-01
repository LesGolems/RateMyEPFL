package com.github.sdp.ratemyepfl.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.RestaurantRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.model.items.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(private val repository: RestaurantRepository) : ViewModel() {

    private val precision: Double = 0.5

    private val nearbyRestaurants: List<Restaurant> = listOf()
    private val insideRestaurant: Restaurant? = null

    private var items = MutableLiveData<List<Restaurant>>()

    init {
        viewModelScope.launch {
            items.value = repository.getRestaurants()
        }
    }

    fun getItemsAsLiveData(): LiveData<List<Restaurant>> {
        return items
    }

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
        repository.getRestaurants().map { r ->
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
