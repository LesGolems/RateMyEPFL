package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.auth.ConnectedUser
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
            reviews.postValue(reviewRepo.getByReviewableId(id))
        }
    }

    fun updateLikers(review: Review) {
        if (!auth.isLoggedIn()) return

        val uid = auth.getUserId()

        if (review.likers.contains(uid)) {
            reviewRepo.removeLiker(review.id, uid!!)
        } else {
            reviewRepo.addLiker(review.id, uid!!)
        }
    }

    fun updateDislikers(review: Review) {
        if (!auth.isLoggedIn()) return

        val uid = auth.getUserId()
        if (review.dislikers.contains(uid)) {
            reviewRepo.removeDisliker(review.id, uid!!)
        } else {
            reviewRepo.addDisliker(review.id, uid!!)
        }
    }
}