package com.github.sdp.ratemyepfl.viewmodel.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepository
import com.github.sdp.ratemyepfl.model.items.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantInfoViewModel @Inject constructor(
    private val restaurantRepo: RestaurantRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED_ID)!!

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