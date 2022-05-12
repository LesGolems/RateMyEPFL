package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepository
import com.github.sdp.ratemyepfl.model.items.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(private val repository: RestaurantRepository) :
    ViewModel() {

    private var items = MutableLiveData<List<Restaurant>>()
    val restaurants = MutableLiveData<List<Restaurant>>()

    init {
        viewModelScope.launch {
            restaurants.value = repository.getRestaurants()
        }
    }
}
