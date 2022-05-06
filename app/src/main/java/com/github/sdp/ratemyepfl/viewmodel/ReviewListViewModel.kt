package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.database.ReviewRepository
import com.github.sdp.ratemyepfl.database.Storage
import com.github.sdp.ratemyepfl.database.UserRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.VoteException
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

    fun updateDownVotes(review: Review, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't like your own review")
        val reviewId = review.getId()

        viewModelScope.launch {
            // The user already disliked the review
            if (review.dislikers.contains(uid)) {
                // Remove a dislike
                reviewRepo.removeDownVote(reviewId, uid)
                userRepo.updateKarma(authorUid, 1)
            } else {
                // The user dislikes for the first time
                if (review.likers.contains(uid)) {
                    // The user changed from like to dislike
                    reviewRepo.removeUpVote(reviewId, uid)
                    userRepo.updateKarma(authorUid, -1)
                }
                // Add a dislike
                reviewRepo.addDownVote(review.getId(), uid)
                userRepo.updateKarma(authorUid, -1)
            }
            updateReviewsList()
        }
    }

    fun updateUpVotes(review: Review, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't like your own review")
        val reviewId = review.getId()

        viewModelScope.launch {
            // The user already liked the review
            if (review.likers.contains(uid)) {
                // Remove a like
                reviewRepo.removeUpVote(reviewId, uid)
                userRepo.updateKarma(authorUid, -1)
            } else {
                // The user likes for the first time
                if (review.dislikers.contains(uid)) {
                    // The user changed from dislike to like
                    reviewRepo.removeDownVote(reviewId, uid)
                    userRepo.updateKarma(authorUid, 1)
                }
                // Add a like
                reviewRepo.addUpVote(review.getId(), uid)
                userRepo.updateKarma(authorUid, 1)
            }
            updateReviewsList()
        }
    }
}