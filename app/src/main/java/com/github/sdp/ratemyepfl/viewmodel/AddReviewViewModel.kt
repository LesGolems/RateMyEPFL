package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.database.ItemsRepositoryInterface
import com.github.sdp.ratemyepfl.database.ReviewsRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val database: ReviewsRepositoryInterface,
    private val itemRepo: ItemsRepositoryInterface
    ) : ViewModel() {

    val rating: MutableLiveData<ReviewRating> = MutableLiveData(null)
    val title: MutableLiveData<String> = MutableLiveData(null)
    val comment: MutableLiveData<String> = MutableLiveData(null)
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

    /**
     * Builds and submits the review to the database
     *
     * @return true if it succeeds to build the review, false otherwise
     */
    fun submitReview(item: Reviewable?): Boolean {
        val rating = rating.value
        val comment = comment.value
        val title = title.value
        val date = date ?: LocalDate.now()

        // For now title is empty, as we don't have an input for it in the UI
        if (rating != null && comment != null && comment != "" && title != null && title != "" && item != null) {
            val review = Review.Builder()
                .setRating(rating)
                .setTitle(title)
                .setComment(comment)
                .setReviewableID(item.id)
                .setDate(date)
                .build()
            database.add(review)
            itemRepo.updateRating(rating, item)
            return true
        }
        return false
    }
}