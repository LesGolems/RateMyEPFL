package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.database.ReviewRepository
import com.github.sdp.ratemyepfl.database.ReviewRepositoryInterface
import com.github.sdp.ratemyepfl.model.review.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * General view model for all activities/fragments of the review part of the app
 */
@HiltViewModel
open class ReviewListViewModel @Inject constructor(
    private val reviewRepo: ReviewRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED)!!

    // Reviews
    val reviews = MutableLiveData<List<Review>>()

    @Inject
    lateinit var auth: ConnectedUser

    init {
        updateReviewsList()
    }

    fun updateReviewsList() {
        viewModelScope.launch {
            reviews.postValue(reviewRepo.getByReviewableId(id)
                .toMutableList()
                .sortedBy { r -> -r.likers.size })
        }
    }

    fun updateVotes(review: Review, array: List<String>, fieldName : String){
        if (!auth.isLoggedIn()) return

        val uid = auth.getUserId() ?: return
        viewModelScope.launch {
            if (array.contains(uid)) {
                reviewRepo.removeUidInArray(fieldName, review.id, uid)
            } else {
                reviewRepo.addUidInArray(fieldName, review.id, uid)
            }
        }
    }

    fun sortByVotes() {
        reviews.value?.let {
            reviews.postValue(it.sortedBy { review -> -review.likers.size })
        }
    }

    fun updateLikes(review: Review) {
        updateVotes(review, review.likers, ReviewRepository.LIKERS_FIELD_NAME)
        updateReviewsList()
    }

    fun updateDislikes(review: Review) {
        updateVotes(review, review.dislikers, ReviewRepository.DISLIKERS_FIELD_NAME)
        updateReviewsList()
    }
}