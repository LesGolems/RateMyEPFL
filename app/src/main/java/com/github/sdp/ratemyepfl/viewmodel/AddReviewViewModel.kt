package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.database.GradeInfoRepository
import com.github.sdp.ratemyepfl.database.post.ReviewRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.model.serializer.getReviewable
import com.github.sdp.ratemyepfl.model.time.Date
import com.github.sdp.ratemyepfl.model.time.DateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

/**
 * View model for the reviewing feature.
 *
 * @constructor: throws an IllegalArgumentException if no reviewable can be induced from
 *               the savedStateHandle
 */
@HiltViewModel
class AddReviewViewModel @Inject constructor(
    private val reviewRepo: ReviewRepository,
    private val gradeInfoRepo: GradeInfoRepository,
    private val connectedUser: ConnectedUser,
    savedStateHandle: SavedStateHandle
) : AddPostViewModel<Review>() {

    companion object {
        const val NO_GRADE_MESSAGE: String = "You need to give a grade !"
    }

    // Id
    val id: String = savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED_ID)!!
    val item = savedStateHandle.getReviewable(ReviewActivity.EXTRA_ITEM_REVIEWED)

    val rating: MutableLiveData<ReviewRating> = MutableLiveData(null)

    /**
     * Set the rating entered by the user
     * @param rating: rating entered by the user
     */
    fun setRating(rating: ReviewRating?) {
        this.rating.postValue(rating)
    }

    /**
     * Builds and submits the review to the database
     *
     * @return the rating of the review or null if the construction didn't work
     * @throws IllegalStateException if the user is not connected, or if one of the fields is empty
     */
    fun submitReview(): ReviewRating? {
        val rating = rating.value
        val comment = comment.value
        val title = title.value
        val date = DateTime.now()
        var uid: String? = null

        // only connected users may add reviews
        if (!connectedUser.isLoggedIn()) {
            throw DisconnectedUserException()
        } else if (comment.isNullOrEmpty()) {
            throw MissingInputException(EMPTY_COMMENT_MESSAGE)
        } else if (title.isNullOrEmpty()) {
            throw MissingInputException(EMPTY_TITLE_MESSAGE)
        } else if (rating == null) {
            throw MissingInputException(NO_GRADE_MESSAGE)
        }

        if (!anonymous.value!!) {
            uid = connectedUser.getUserId()
        }

        val builder = Review.Builder()
            .setRating(rating)
            .setReviewableID(id)
            .setTitle(title)
            .setComment(comment)
            .setDate(date)
            .setUid(uid)
        try {
            val review = builder.build()
            viewModelScope.launch(Dispatchers.IO) {
                val reviewId = reviewRepo.addAndGetId(review)
                gradeInfoRepo.addReview(item, reviewId, review.rating).await()
            }
        } catch (e: IllegalStateException) {
            throw IllegalStateException("Failed to build the review (from ${e.message}")
        }

        return rating
    }
}