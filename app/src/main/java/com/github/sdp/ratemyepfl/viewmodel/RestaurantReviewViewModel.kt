package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.database.ItemsRepositoryInterface
import com.github.sdp.ratemyepfl.database.ReviewsRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantReviewViewModel @Inject constructor(
    private val reviewsRepository: ReviewsRepositoryInterface,
    private val itemsRepository: ItemsRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ReviewViewModel(reviewsRepository, itemsRepository, savedStateHandle) {

    // Restaurant
    private val restaurant = MutableLiveData<Restaurant?>()

    init {
        updateRoom()
        updateReviewsList()
    }

    fun updateRoom() {
        viewModelScope.launch {
            restaurant.value = itemsRepository.getByIdRestaurants(id!!)
        }
    }

    fun getRestaurant(): LiveData<Restaurant?> {
        return restaurant
    }
}