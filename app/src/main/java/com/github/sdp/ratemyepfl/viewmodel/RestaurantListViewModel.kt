package com.github.sdp.ratemyepfl.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.query.OrderDirection
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepository
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.viewmodel.filter.ClassroomFilter
import com.github.sdp.ratemyepfl.viewmodel.filter.RestaurantFilter
import com.github.sdp.ratemyepfl.viewmodel.filter.ReviewableFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class RestaurantListViewModel @Inject constructor(private val repository: RestaurantRepositoryImpl) :
    ReviewableListViewModel<Restaurant>(
        repository,
        RestaurantRepositoryImpl.RESTAURANT_NAME_FIELD_NAME,
        RestaurantFilter.BestRated,
    ) {

    val restaurants: MutableLiveData<List<Restaurant>> = elements


    companion object {
        private fun alphabeticalOrderQuery(repository: RestaurantRepository) = repository
            .query()
            .orderBy(RestaurantRepositoryImpl.RESTAURANT_NAME_FIELD_NAME)


        private fun alphabeticalOrderReversedQuery(repository: RestaurantRepository) = repository
            .query()
            .orderBy(RestaurantRepositoryImpl.RESTAURANT_NAME_FIELD_NAME, OrderDirection.DESCENDING)
    }

    private val precision: Double = 0.0001

    private val nearbyRestaurants: List<Restaurant> = listOf()
    private var insideRestaurantId: String? = null

    private var items = MutableLiveData<List<Restaurant>>()

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
                    if (insideRestaurantId != null) {
                        repository.decrementOccupancy(insideRestaurantId!!)
                    }
                    id
                }
            }
        }
    }

    private suspend fun findCloseRestaurantID(l: Location): String? {
        repository.getRestaurants().map { r ->
            if (isClose(l.latitude, r.lat, l.longitude, r.long)) {
                return r.name
            }
        }
        return null
    }

    private fun isClose(lat1: Double, lat2: Double, long1: Double, long2: Double): Boolean {
        return (abs(lat1 - lat2) < precision) && (abs(long1 - long2) < precision)
    }

}
