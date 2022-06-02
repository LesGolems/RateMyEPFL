package com.github.sdp.ratemyepfl.viewmodel.review

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.backend.auth.ConnectedUser
import com.github.sdp.ratemyepfl.backend.database.GradeInfoRepository
import com.github.sdp.ratemyepfl.backend.database.Storage
import com.github.sdp.ratemyepfl.backend.database.UserRepository
import com.github.sdp.ratemyepfl.backend.database.post.ReviewRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.VoteException
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewWithAuthor
import com.github.sdp.ratemyepfl.model.serializer.getReviewable
import com.github.sdp.ratemyepfl.ui.activity.ReviewActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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

    val isEmpty: LiveData<Boolean> = reviews.map { it.isEmpty() }

    @Inject
    lateinit var auth: ConnectedUser

    fun getReviews(): Flow<List<ReviewWithAuthor>> =
        reviewRepo.getByReviewableId(id)
            .map { reviews ->
                reviews.map { review ->
                    ReviewWithAuthor(
                        review,
                        review.uid?.let { userRepo.getUserByUid(it) },
                        review.uid?.let { imageStorage.get(it).lastOrNull() }
                    )
                }
                    .sortedBy { rwa -> -rwa.obj.likers.size }
            }

    fun removeReview(reviewId: String) {
        viewModelScope.launch {
            reviewRepo.remove(reviewId).collect()
            gradeInfoRepo.removeReview(itemReviewed, reviewId)
        }
    }

    fun updateDownVotes(review: Review, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't dislike your own review")
        val reviewId = review.getId()
        var posts = reviews.value

        viewModelScope.launch {
            // The user already disliked the review
            if (review.dislikers.contains(uid)) {
                // Remove a dislike
                reviewRepo.removeDownVote(reviewId, uid)
                userRepo.updateKarma(authorUid, 1)
                gradeInfoRepo.updateLikeRatio(itemReviewed, reviewId, 1)
                posts = removeDislikeInLiveData(review.getId(), uid, posts)
            } else {
                // The user dislikes for the first time
                if (review.likers.contains(uid)) {
                    // The user changed from like to dislike
                    reviewRepo.removeUpVote(reviewId, uid)
                    userRepo.updateKarma(authorUid, -1)
                    gradeInfoRepo.updateLikeRatio(itemReviewed, reviewId, -1)
                    posts = removeLikeInLiveData(review.getId(), uid, posts)
                }
                // Add a dislike
                reviewRepo.addDownVote(review.getId(), uid)
                userRepo.updateKarma(authorUid, -1)
                gradeInfoRepo.updateLikeRatio(itemReviewed, reviewId, -1)
                posts = addDislikeInLiveData(review.getId(), uid, posts)
            }
            posts.let { reviews.postValue(it) }
        }
    }

    private fun addLikeInLiveData(id: String, uid: String?, posts: List<ReviewWithAuthor>?) =
        posts?.map {
            val review = it.obj
            if (review.getId() == id && uid != null) {
                ReviewWithAuthor(
                    review.copy(likers = review.likers.plus(uid)),
                    it.author,
                    it.image
                )
            } else {
                it
            }
        }?.sortedBy { rwa -> -rwa.obj.likers.size }

    private fun removeLikeInLiveData(id: String, uid: String?, posts: List<ReviewWithAuthor>?) =
        posts?.map {
            val review = it.obj
            if (review.getId() == id && uid != null) {
                ReviewWithAuthor(
                    review.copy(likers = review.likers.minus(uid)),
                    it.author,
                    it.image
                )
            } else {
                it
            }
        }?.sortedBy { rwa -> -rwa.obj.likers.size }

    private fun addDislikeInLiveData(id: String, uid: String?, posts: List<ReviewWithAuthor>?) =
        posts?.map {
            val review = it.obj
            if (review.getId() == id && uid != null) {
                ReviewWithAuthor(
                    review.copy(dislikers = review.dislikers.plus(uid)),
                    it.author,
                    it.image
                )
            } else {
                it
            }
        }?.sortedBy { rwa -> -rwa.obj.likers.size }

    private fun removeDislikeInLiveData(id: String, uid: String?, posts: List<ReviewWithAuthor>?) =
        posts?.map {
            val review = it.obj
            if (review.getId() == id && uid != null) {
                ReviewWithAuthor(
                    review.copy(dislikers = review.dislikers.minus(uid)),
                    it.author,
                    it.image
                )
            } else {
                it
            }
        }?.sortedBy { rwa -> -rwa.obj.likers.size }


    fun updateUpVotes(review: Review, authorUid: String?) {
        val uid = auth.getUserId() ?: throw DisconnectedUserException()
        if (uid == authorUid) throw VoteException("You can't like your own review")
        val reviewId = review.getId()
        var posts = reviews.value

        viewModelScope.launch {
            // The user already liked the review
            if (review.likers.contains(uid)) {
                // Remove a like
                reviewRepo.removeUpVote(reviewId, uid)
                userRepo.updateKarma(authorUid, -1)
                gradeInfoRepo.updateLikeRatio(itemReviewed, reviewId, -1)
                posts = removeLikeInLiveData(review.getId(), uid, posts)
            } else {
                // The user likes for the first time
                if (review.dislikers.contains(uid)) {
                    // The user changed from dislike to like
                    reviewRepo.removeDownVote(reviewId, uid)
                    userRepo.updateKarma(authorUid, 1)
                    gradeInfoRepo.updateLikeRatio(itemReviewed, reviewId, 1)
                    posts = removeDislikeInLiveData(review.getId(), uid, posts)
                }
                // Add a like
                reviewRepo.addUpVote(review.getId(), uid)
                userRepo.updateKarma(authorUid, 1)
                gradeInfoRepo.updateLikeRatio(itemReviewed, reviewId, 1)
                posts = addLikeInLiveData(review.getId(), uid, posts)
            }
            posts.let { reviews.postValue(it) }
        }
    }
}