package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.database.ReviewRepository
import com.github.sdp.ratemyepfl.database.ReviewRepositoryImpl
import com.github.sdp.ratemyepfl.database.Storage
import com.github.sdp.ratemyepfl.database.UserRepository
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewWithAuthor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * General view model for all activities/fragments of the review part of the app
 */
@HiltViewModel
open class ReviewListViewModel @Inject constructor(
    private val reviewRepo: ReviewRepository,
    private val userRepo: UserRepository,
    private val imageStorage: Storage<ImageFile>,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED)!!

    // Reviews
    val reviews = MutableLiveData<List<ReviewWithAuthor>>()

    @Inject
    lateinit var auth: ConnectedUser

    init {
        updateReviewsList()
    }

    fun updateReviewsList() {
        viewModelScope.launch {
            reviews.postValue(reviewRepo.getByReviewableId(id)
                .toMutableList()
                .map { review ->
                    ReviewWithAuthor(
                        review,
                        review.uid?.let { userRepo.getUserByUid(it) },
                        review.uid?.let { imageStorage.get(it) }
                    )
                }
                .sortedBy { rwa -> -rwa.review.likers.size })
        }
    }

    fun updateVotes(review: Review, array: List<String>, fieldName: String) {
        if (!auth.isLoggedIn()) return

        val uid = auth.getUserId() ?: return
        viewModelScope.launch {
            if (array.contains(uid)) {
                reviewRepo.removeUidInArray(fieldName, review.getId(), uid)
            } else {
                reviewRepo.addUidInArray(fieldName, review.getId(), uid)
            }
            updateReviewsList()
        }
    }

    fun sortByVotes() {
        reviews.value?.let {
            reviews.postValue(it.sortedBy { rwa -> -rwa.review.likers.size })
        }
    }

    fun updateLikes(review: Review) {
        updateVotes(review, review.likers, ReviewRepositoryImpl.LIKERS_FIELD_NAME)
    }

    fun updateDislikes(review: Review) {
        updateVotes(review, review.dislikers, ReviewRepositoryImpl.DISLIKERS_FIELD_NAME)
    }
}