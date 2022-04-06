package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.RestaurantRepositoryInterface
import com.github.sdp.ratemyepfl.database.ReviewsRepository
import com.github.sdp.ratemyepfl.model.items.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantInfoViewModel @Inject constructor(
    private val reviewRepo: ReviewsRepository,
    private val restaurantRepo: RestaurantRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ReviewViewModel(
    reviewRepo, savedStateHandle
) {
    val restaurant = MutableLiveData<Restaurant>()

    init {
        updateRestaurant()
    }

    fun updateRestaurant() {
        viewModelScope.launch {
            restaurant.postValue(restaurantRepo.getRestaurantById(id))
        }
    }
}