package com.github.sdp.ratemyepfl.viewmodel.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.backend.database.GradeInfoRepository
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.backend.database.UserRepository
import com.github.sdp.ratemyepfl.backend.database.post.ReviewRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.VoteException
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.PostWithAuthor
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewWithAuthor
import com.github.sdp.ratemyepfl.model.serializer.getReviewable
import com.github.sdp.ratemyepfl.ui.activity.ReviewActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * General view model for all activities/fragments of the review part of the app
 */
@HiltViewModel
open class ReviewListViewModel @Inject constructor(
    private val reviewRepo: ReviewRepository,
    private val userRepo: UserRepository,
    private val gradeInfoRepo: GradeInfoRepository,
    private val imageStorage: Storage<ImageFile>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Id
    val id: String = savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED_ID)!!

    private val itemReviewed = savedStateHandle.getReviewable(ReviewActivity.EXTRA_ITEM_REVIEWED)

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
                    PostWithAuthor(
                        review,
                        review.uid?.let { userRepo.getUserByUid(it) },
                        review.uid?.let { imageStorage.get(it).lastOrNull() }
                    )
                }
                .sortedBy { rwa -> -rwa.post.likers.size })
        }
    }

    fun removeReview(reviewId: String) {
        viewModelScope.launch {
            reviewRepo.remove(reviewId).await()
            gradeInfoRepo.removeReview(itemReviewed, reviewId)
            updateReviewsList()
        }
    }

    fun updateDownVotes(review: Review, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't dislike your own review")
        val reviewId = review.getId()

        viewModelScope.launch {
            // The user already disliked the review
            if (review.dislikers.contains(uid)) {
                // Remove a dislike
                reviewRepo.removeDownVote(reviewId, uid)
                userRepo.updateKarma(authorUid, 1)
                gradeInfoRepo.updateLikeRatio(itemReviewed, reviewId, 1)
            } else {
                // The user dislikes for the first time
                if (review.likers.contains(uid)) {
                    // The user changed from like to dislike
                    reviewRepo.removeUpVote(reviewId, uid)
                    userRepo.updateKarma(authorUid, -1)
                    gradeInfoRepo.updateLikeRatio(itemReviewed, reviewId, -1)
                }
                // Add a dislike
                reviewRepo.addDownVote(review.getId(), uid)
                userRepo.updateKarma(authorUid, -1)
                gradeInfoRepo.updateLikeRatio(itemReviewed, reviewId, -1)
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
                gradeInfoRepo.updateLikeRatio(itemReviewed, reviewId, -1)
            } else {
                // The user likes for the first time
                if (review.dislikers.contains(uid)) {
                    // The user changed from dislike to like
                    reviewRepo.removeDownVote(reviewId, uid)
                    userRepo.updateKarma(authorUid, 1)
                    gradeInfoRepo.updateLikeRatio(itemReviewed, reviewId, 1)
                }
                // Add a like
                reviewRepo.addUpVote(review.getId(), uid)
                userRepo.updateKarma(authorUid, 1)
                gradeInfoRepo.updateLikeRatio(itemReviewed, reviewId, 1)
            }
            updateReviewsList()
        }
    }
}