package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.RestaurantRepositoryInterface
import com.github.sdp.ratemyepfl.database.ReviewsRepository
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantInfoViewModel @Inject constructor(
    private val restaurantRepo: RestaurantRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED)!!

    val restaurant = MutableLiveData<Restaurant>()

    init {
        updateRestaurant()
    }

    fun updateRestaurant() {
        viewModelScope.launch {
            restaurant.postValue(restaurantRepo.getRestaurantById(id))
        }
    }

    fun updateRating(rating: ReviewRating) {
        viewModelScope.launch {
            restaurantRepo.updateRestaurantRating(id, rating)
        }
    }
}