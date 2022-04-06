package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.R
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

    companion object {
        const val NO_GRADE = 0
    }

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED)!!

    // Reviews
    val reviews = MutableLiveData<List<Review>>()

    val numReviews: LiveData<Int> = computeNumReviews()

    // Fake photo ids
    private val fakePhotoIds = listOf(
        R.drawable.room3,
        R.drawable.room1,
        R.drawable.room4,
        R.drawable.room2,
        R.drawable.room5,
        R.drawable.room6
    )

    val overallGrade: LiveData<Int> = computeOverallGrade()

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

    /**
     * Returns the list of photos as LiveData
     */
    fun getPhotos(): LiveData<List<Int>> {
        return MutableLiveData(fakePhotoIds)
    }
}