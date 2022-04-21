package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.database.ReviewRepository
import com.github.sdp.ratemyepfl.database.ReviewRepositoryInterface
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
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
    private val reviewRepo: ReviewRepositoryInterface,
    private val connectedUser: ConnectedUser
) : ViewModel() {

    val rating: MutableLiveData<ReviewRating> = MutableLiveData(null)
    val title: MutableLiveData<String> = MutableLiveData(null)
    val comment: MutableLiveData<String> = MutableLiveData(null)
    var anonymous: MutableLiveData<Boolean> = MutableLiveData(false)
    private var date: LocalDate? = null

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
    fun submitReview(id: String): ReviewRating? {
        val rating = rating.value
        val comment = comment.value
        val title = title.value
        val date = date ?: LocalDate.now()
        var uid: String? = null

        // only connected users may add reviews
        if (!connectedUser.isLoggedIn()) {
            throw IllegalStateException("You must be connected to add a review.")
        } else if (comment == null || comment == "") {
            throw IllegalStateException("You must add a comment.")
        } else if (title == null || title == "") {
            throw IllegalStateException("You must add a title.")
        } else if (rating == null) {
            throw IllegalStateException("You must add a star rating.")
        }

        if (!anonymous.value!!) {
            uid = connectedUser.getUserId()
        }

        val reviewHashMap = hashMapOf(
            ReviewRepository.TITLE_FIELD_NAME to title,
            ReviewRepository.RATING_FIELD_NAME to rating.toString(),
            ReviewRepository.COMMENT_FIELD_NAME to comment,
            ReviewRepository.REVIEWABLE_ID_FIELD_NAME to id,
            ReviewRepository.DATE_FIELD_NAME to date.toString(),
            ReviewRepository.UID_FIELD_NAME to uid, // will add the user next sprint
            ReviewRepository.LIKERS_FIELD_NAME to listOf<String>(),
            ReviewRepository.DISLIKERS_FIELD_NAME to listOf<String>()
        )

        viewModelScope.launch(Dispatchers.IO) {
            reviewRepo.add(reviewHashMap)
        }

        return rating
    }
}