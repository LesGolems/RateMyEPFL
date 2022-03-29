package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import com.github.sdp.ratemyepfl.database.ItemRepository
import com.github.sdp.ratemyepfl.model.items.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(repository: ItemRepository<Restaurant>) :
    ReviewableListViewModel<Restaurant>(repository) {

    fun getRestaurants(): LiveData<List<Restaurant>> {
        return getItemsAsLiveData()
    }

}