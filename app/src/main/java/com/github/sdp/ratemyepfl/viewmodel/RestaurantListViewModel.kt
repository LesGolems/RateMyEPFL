package com.github.sdp.ratemyepfl.viewmodel

import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.viewmodel.filter.RestaurantFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(private val repository: RestaurantRepositoryImpl) :
    ReviewableListViewModel<Restaurant>(
        repository,
        RestaurantRepositoryImpl.RESTAURANT_NAME_FIELD_NAME,
        RestaurantFilter.BestRated,
    ) {
}
