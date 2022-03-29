package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.ReviewsRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ReviewViewModel @Inject constructor(
    private val reviewRepo: ReviewsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val id: String? = savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED)

    // Reviews
    private val reviewsLiveData = MutableLiveData<List<Review>>()
    // Reviewable
    private val reviewable = MutableLiveData<Reviewable?>()

    init {
        updateReviewsList()
    }

    fun updateReviewsList() {
        viewModelScope.launch {
            reviewsLiveData.value = reviewRepo.getByReviewableId(id)
        }
    }


    fun getNumReviews(): LiveData<Int>{
        return Transformations.switchMap(
            reviewsLiveData
        ) { reviewList ->
            MutableLiveData(reviewList.size)
        }
    }

    fun getOverallGrade(): LiveData<Int>{
        return Transformations.switchMap(
            reviewsLiveData
        ) { reviewList ->
            val sumOfRates = reviewList.map { it.rating.toValue() }.sum()
            MutableLiveData(sumOfRates/reviewList.size)
        }
    }

    fun getReviews(): LiveData<List<Review>> {
        return reviewsLiveData
    }

    fun getReviewable(): LiveData<Reviewable?> {
        return reviewable
    }
}