package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.activity.AddReviewActivity
import com.github.sdp.ratemyepfl.database.ItemsRepositoryInterface
import com.github.sdp.ratemyepfl.database.ReviewsRepositoryInterface
import com.github.sdp.ratemyepfl.model.review.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ReviewViewModel @Inject constructor(
    private val reviewsRepository: ReviewsRepositoryInterface,
    private val itemsRepository: ItemsRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val id: String? = savedStateHandle.get<String>(AddReviewActivity.EXTRA_ITEM_REVIEWED)

    // Reviews
    private val reviewsLiveData = MutableLiveData<List<Review?>>()


    fun updateReviewsList() {
        viewModelScope.launch {
            reviewsLiveData.value = reviewsRepository.getByReviewableId(id)
        }
    }

    fun getReviews(): LiveData<List<Review?>> {
        return reviewsLiveData
    }
}