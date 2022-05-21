package com.github.sdp.ratemyepfl.viewmodel.main

import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.viewmodel.filter.RestaurantFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(private val repository: RestaurantRepositoryImpl) :
    ReviewableListViewModel<Restaurant>(
        repository,
        RestaurantRepositoryImpl.RESTAURANT_NAME_FIELD_NAME,
        RestaurantFilter.BestRated,
    ) {

    init {
        viewModelScope.launch {
            elements.value = repository.getRestaurants()
        }
    }
}
