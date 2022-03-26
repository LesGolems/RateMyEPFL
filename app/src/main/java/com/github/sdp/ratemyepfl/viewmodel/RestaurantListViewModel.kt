package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.ItemsRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(private val repository: ItemsRepositoryInterface) :
    ViewModel() {

    private var restaurantLiveData = MutableLiveData<List<Restaurant>>()

    init {
        viewModelScope.launch {
            restaurantLiveData.value = repository.getRestaurants()
        }
    }

    fun getRestaurants(): LiveData<List<Restaurant>> {
        return restaurantLiveData
    }

}