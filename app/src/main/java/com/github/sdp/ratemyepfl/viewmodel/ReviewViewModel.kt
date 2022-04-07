package com.github.sdp.ratemyepfl.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.auth.ConnectedUser
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

    @Inject
    lateinit var auth: ConnectedUser
    //var opinion: ReviewOpinion = ReviewOpinion.NO_OPINION

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

    //val FAKE_UID = "FAKE ID"

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

    /*fun setOpinion(review: Review) {
        if (!auth.isLoggedIn())
            opinion = ReviewOpinion.NO_OPINION
        //val uid = auth.getUserId()
        if (review.likers.contains(FAKE_UID))
            opinion = ReviewOpinion.LIKED
        if (review.dislikers.contains(FAKE_UID))
            opinion = ReviewOpinion.DISLIKED
        Log.d("opinion", opinion.name)
    }
    fun getOpinion(review: Review): ReviewOpinion {
        return opinion
    }*/

    fun updateLikers(review: Review) {
        val uid = auth.getUserId()
        if (review.likers.contains(uid)) {
            reviewRepo.removeLiker(review.id!!, uid!!)
        } else {
            reviewRepo.addLiker(review.id!!, uid!!)
        }
    }

    fun updateDislikers(review: Review) {
        val uid = auth.getUserId()
        if (review.dislikers.contains(uid)) {
            reviewRepo.removeDisliker(review.id!!, uid!!)
        } else {
            reviewRepo.addDisliker(review.id!!, uid!!)
        }
    }

}