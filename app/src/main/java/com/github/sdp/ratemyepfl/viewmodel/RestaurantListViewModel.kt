package com.github.sdp.ratemyepfl.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.RestaurantRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.google.firebase.firestore.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(private val repository: RestaurantRepository) :
    ViewModel() {

    private val precision: Double = 0.0001

    private val nearbyRestaurants: List<Restaurant> = listOf()
    private var insideRestaurantId: String? = null

    private var items = MutableLiveData<List<Restaurant>>()

    init {
        viewModelScope.launch {
            items.value = repository.getRestaurants()
        }
    }

    fun getItemsAsLiveData(): LiveData<List<Restaurant>> {
        return items
    }


    fun getRestaurantByLocation(location: Location): LiveData<List<Restaurant>> {
        TODO()
    }

    fun updateRestaurantsOccupancy(l: Location) {
        viewModelScope.launch {
            val id = findCloseRestaurantID(l)
            if (id != insideRestaurantId) {
                insideRestaurantId = if (id == null) {
                    repository.decrementOccupancy(insideRestaurantId!!)
                    null
                } else {
                    repository.incrementOccupancy(id)
                    repository.decrementOccupancy(insideRestaurantId!!)
                    id
                }
            }
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
