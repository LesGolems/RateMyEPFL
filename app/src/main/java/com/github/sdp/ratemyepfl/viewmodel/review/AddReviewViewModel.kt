package com.github.sdp.ratemyepfl.viewmodel.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.database.GradeInfoRepository
import com.github.sdp.ratemyepfl.database.ReviewRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.model.serializer.getReviewable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

/**
 * View model for the course reviewing feature.
 *
 * @constructor: throws an IllegalArgumentException if no course can be induced from
 *               the savedStateHandle
 */
@HiltViewModel
class AddReviewViewModel @Inject constructor(
    private val reviewRepo: ReviewRepository,
    private val gradeInfoRepo: GradeInfoRepository,
    private val connectedUser: ConnectedUser,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED_ID)!!

    val item = savedStateHandle.getReviewable(ReviewActivity.EXTRA_ITEM_REVIEWED)

    companion object {
        const val EMPTY_TITLE_MESSAGE: String = "Please enter a title"
        const val EMPTY_COMMENT_MESSAGE: String = "Please enter a comment"
        const val NO_GRADE_MESSAGE: String = "You need to give a grade !"
    }

    val rating: MutableLiveData<ReviewRating> = MutableLiveData(null)
    val title: MutableLiveData<String> = MutableLiveData(null)
    val comment: MutableLiveData<String> = MutableLiveData(null)
    private var anonymous: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * Set the rating entered by the user
     * @param rating: rating entered by the user
     */
    fun setRating(rating: ReviewRating?) {
        this.rating.postValue(rating)
    }

    /**
     * Set the title entered by the user
     * @param title: title entered by the user
     */
    fun setTitle(title: String?) = this.title.postValue(title)

    /**
     * Set the comment entered by the user
     * @param comment: comment entered by the user
     */
    fun setComment(comment: String?) = this.comment.postValue(comment)

    fun setAnonymous(anonymous: Boolean) {
        this.anonymous.postValue(anonymous)
    }

    /**
     * Builds and submits the review to the database
     *
     * @return the rating of the review or null if the construction didn't work
     * @throws IllegalStateException if the user is not connected, or if one of the fields is empty
     */
    fun submitReview(): ReviewRating {
        val rating = rating.value
        val comment = comment.value
        val title = title.value
        val date = LocalDate.now()
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
            .setTitle(title)
            .setRating(rating)
            .setComment(comment)
            .setReviewableID(id)
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