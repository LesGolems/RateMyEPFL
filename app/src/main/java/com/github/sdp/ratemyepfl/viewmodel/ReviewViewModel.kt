package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.ReviewsRepository
import com.github.sdp.ratemyepfl.model.review.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * General view model for all activities/fragments of the review part of the app
 */
@HiltViewModel
open class ReviewViewModel @Inject constructor(
    private val reviewRepo: ReviewsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object{
        const val NO_GRADE = 10
    }

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED)!!

    // Reviews
    private val reviewsLiveData = MutableLiveData<List<Review>>()

    val numReviews : LiveData<Int> = computeNumReviews()

    val overallGrade : LiveData<Int> = computeOverallGrade()

    init {
        updateReviewsList()
    }

    fun updateReviewsList() {
        viewModelScope.launch {
            reviewsLiveData.postValue(reviewRepo.getByReviewableId(id))
        }
    }


    /**
     * Returns the numbers of reviews of the current reviewed item as LiveData
     */
    private fun computeNumReviews(): LiveData<Int> {
        return Transformations.switchMap(
            reviewsLiveData
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
            reviewsLiveData
        ) { reviewList ->
            if(reviewList.isEmpty()) MutableLiveData(NO_GRADE)
            val sumOfRates = reviewList.sumOf { it.rating.toValue() }
            MutableLiveData(sumOfRates / reviewList.size)
        }
    }

    /**
     * Returns the list of review as LiveData
     */
    fun getReviews(): LiveData<List<Review>> {
        return reviewsLiveData
    }
}