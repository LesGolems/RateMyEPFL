package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.ItemsRepositoryInterface
import com.github.sdp.ratemyepfl.database.ReviewsRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
General view model for all activities/fragments of the review part of the app
 */
@HiltViewModel
open class ReviewViewModel @Inject constructor(
    private val reviewRepo: ReviewsRepositoryInterface,
    private val itemRepo: ItemsRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val id: String? = savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED)

    // Reviews
    private val reviewsLiveData = MutableLiveData<List<Review>>()
    // Reviewable
    private val reviewable = MutableLiveData<Reviewable?>()

    init {
        updateReviewable()
        updateReviewsList()
    }

    fun updateReviewsList() {
        viewModelScope.launch {
            reviewsLiveData.value = reviewRepo.getByReviewableId(id)
        }
    }

    fun updateReviewable() {
        viewModelScope.launch {
            reviewable.value = itemRepo.getById(id)
        }
    }

    /**
     * Returns the numbers of reviews of the current reviewed item as LiveData
     */
    fun getNumReviews(): LiveData<Int>{
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
    fun getOverallGrade(): LiveData<Int>{
        return Transformations.switchMap(
            reviewsLiveData
        ) { reviewList ->
            val sumOfRates = reviewList.map { it.rating.toValue() }.sum()
            MutableLiveData(sumOfRates/reviewList.size)
        }
    }

    /**
     * Returns the list of review as LiveData
     */
    fun getReviews(): LiveData<List<Review>> {
        return reviewsLiveData
    }

    /**
     * Returns the current reviewed item as LiveData
     */
    fun getReviewable(): LiveData<Reviewable?> {
        return reviewable
    }
}