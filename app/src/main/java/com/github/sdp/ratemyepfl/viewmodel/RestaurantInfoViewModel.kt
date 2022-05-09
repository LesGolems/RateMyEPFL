package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.GradeInfoRepository
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepository
import com.github.sdp.ratemyepfl.model.items.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantInfoViewModel @Inject constructor(
    private val restaurantRepo: RestaurantRepository,
    private val gradeInfoRepo: GradeInfoRepository,
    private val savedStateHandle: SavedStateHandle
) : ReviewableInfoViewModel(gradeInfoRepo, savedStateHandle) {

    val restaurant = MutableLiveData<Restaurant>()

    init {
        refresh()
    }

    fun updateRestaurant() {
        viewModelScope.launch {
            restaurant.postValue(restaurantRepo.getRestaurantById(id))
        }
    }

    fun refresh() {
        updateRestaurant()
        refreshGrade()
    }
}