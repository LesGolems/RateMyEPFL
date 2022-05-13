package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.query.OrderDirection
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepository
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.viewmodel.filter.ClassroomFilter
import com.github.sdp.ratemyepfl.viewmodel.filter.RestaurantFilter
import com.github.sdp.ratemyepfl.viewmodel.filter.ReviewableFilter
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
}
