package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.database.ReviewRepository
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.IllegalStateException
import java.time.LocalDate
import javax.inject.Inject
import kotlin.IllegalStateException

/**
 * View model for the course reviewing feature.
 *
 * @constructor: throws an IllegalArgumentException if no course can be induced from
 *               the savedStateHandle
 */
@HiltViewModel
class AddReviewViewModel @Inject constructor(
    private val reviewRepo: ReviewRepository,
    private val connectedUser: ConnectedUser
) : ViewModel() {

    val rating: MutableLiveData<ReviewRating> = MutableLiveData(null)
    val title: MutableLiveData<String> = MutableLiveData(null)
    val comment: MutableLiveData<String> = MutableLiveData(null)
    var anonymous: MutableLiveData<Boolean> = MutableLiveData(false)

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
        val date = LocalDate.now()
        var uid: String? = null

        // only connected users may add reviews
        if (!connectedUser.isLoggedIn()) {
            return null
        } else if (comment == null || comment == "") {
            return null
        } else if (title == null || title == "") {
            return null
        } else if (rating == null) {
            return null
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
                reviewRepo.add(review).await()
            }
        } catch (e: IllegalStateException) {
            throw IllegalStateException("Failed to build the review (from ${e.message}")
        }

        return rating
    }
}