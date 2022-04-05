package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.RestaurantRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(repository: RestaurantRepositoryInterface) :
    ViewModel() {

    private var items = MutableLiveData<List<Restaurant>>()

    init {
        viewModelScope.launch {
            items.value = repository.getRestaurants()
        }
    }

    fun getItemsAsLiveData(): LiveData<List<Restaurant>> {
        return items
    }
}