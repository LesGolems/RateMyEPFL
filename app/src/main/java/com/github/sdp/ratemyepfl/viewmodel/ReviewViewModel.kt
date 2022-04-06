package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.ReviewRepositoryInterface
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewOpinion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * General view model for all activities/fragments of the review part of the app
 */
@HiltViewModel
open class ReviewViewModel @Inject constructor(
    private val reviewRepo: ReviewRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val NO_GRADE = 0
    }

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED)!!

    // Reviews
    val reviews = MutableLiveData<List<Review>>()

    val numReviews: LiveData<Int> = computeNumReviews()

    val overallGrade: LiveData<Int> = computeOverallGrade()

    val currentReview = MutableLiveData<Review>()

    init {
        updateReviewsList()
    }

    fun updateReviewsList() {
        viewModelScope.launch {
            reviews.postValue(reviewRepo.getByReviewableId(id))
        }
    }


    /**
     * Returns the numbers of reviews of the current reviewed item as LiveData
     */
    private fun computeNumReviews(): LiveData<Int> {
        return Transformations.switchMap(
            reviews
        ) { reviewList ->
            MutableLiveData(reviewList.size)
        }
    }

    /**
     * Returns the overall grade of the current reviewed item as LiveData
     * (Note that, for concurrency issues, we calculate the overall grade using the list of reviews)
     */
    private fun computeOverallGrade(): LiveData<Int> {
        return Transformations.switchMap(
            reviews
        ) { reviewList ->
            if (reviewList.isEmpty()) {
                MutableLiveData(NO_GRADE)
            } else {
                val sumOfRates = reviewList.sumOf { it.rating.toValue() }
                MutableLiveData(sumOfRates / reviewList.size)
            }
        }
    }

    fun updateCurrentReview(review: Review) {
        viewModelScope.launch {
            currentReview.postValue(reviewRepo.getReviewById(review.id!!))
        }
    }

    fun getOpinion(review: Review): ReviewOpinion? {
        viewModelScope.launch {
            currentReview.postValue(reviewRepo.getReviewById(review.id!!))
        }
        return currentReview.value?.opinion
    }

    fun setOpinion(review: Review, opinion: ReviewOpinion) {
        viewModelScope.launch {
            review.id?.let { reviewRepo.setOpinion(it, opinion) }
        }
    }

    fun updateLikes(id: String, quantity: Int) {
        reviewRepo.updateLikes(id, quantity)
    }

    fun updateDislikes(id: String, quantity: Int) {
        reviewRepo.updateDislikes(id, quantity)
    }

}