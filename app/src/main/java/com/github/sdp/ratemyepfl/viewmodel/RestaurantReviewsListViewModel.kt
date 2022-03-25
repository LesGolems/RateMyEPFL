package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.activity.restaurants.RestaurantReviewListActivity
import com.github.sdp.ratemyepfl.database.ReviewsRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class RestaurantReviewsListViewModel @Inject constructor(
    private val reviewsRepository: ReviewsRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val restaurant: Restaurant? =
        savedStateHandle.get<String>(RestaurantReviewListActivity.EXTRA_RESTAURANT_JSON)
            ?.let { Json.decodeFromString(it) }

    private val reviewsLiveData = MutableLiveData<List<Review?>>()

    init {
        updateReviewsList()
    }

    fun updateReviewsList() {
        viewModelScope.launch {
            reviewsLiveData.value = reviewsRepository.getByReviewableId(restaurant?.id)
        }
    }

    fun getReviews(): LiveData<List<Review?>> {
        return reviewsLiveData
    }


}