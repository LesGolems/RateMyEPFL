package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.database.ReviewRepositoryInterface
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val reviewRepo: ReviewRepositoryInterface
) : ViewModel() {

    val rating: MutableLiveData<ReviewRating> = MutableLiveData(null)
    val title: MutableLiveData<String> = MutableLiveData(null)
    val comment: MutableLiveData<String> = MutableLiveData(null)

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

    /**
     * Builds and submits the review to the database
     *
     * @return the rating of the review or null if the construction didn't work
     */
    fun submitReview(id: String): ReviewRating? {
        val rating = rating.value
        val comment = comment.value
        val title = title.value
        val date = LocalDate.now()

        if (comment == null || comment == "") return null
        if (title == null || title == "") return null
        if (rating == null) return null

        val builder = Review.Builder()
            .setTitle(title)
            .setRating(rating)
            .setComment(comment)
            .setReviewableID(id)
            .setDate(date)
        try {
            val review = builder.build()
            reviewRepo.addAsync(review)
        } catch (e: IllegalStateException) { }

        return rating
    }
}