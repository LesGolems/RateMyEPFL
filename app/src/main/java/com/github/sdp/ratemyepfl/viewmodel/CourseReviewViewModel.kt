package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.activity.CourseReviewActivity
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.CourseReview
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import javax.inject.Inject

/**
 * View model for the course reviewing feature.
 *
 * @constructor: throws an IllegalArgumentException if no course can be induced from
 *               the savedStateHandle
 */
@HiltViewModel
class CourseReviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val rating: MutableLiveData<ReviewRating> = MutableLiveData(null)
    val title: MutableLiveData<String> = MutableLiveData(null)
    val comment: MutableLiveData<String> = MutableLiveData(null)
    private var date: LocalDate? = null

    // Retrieve the reviewed course from the savedStateHandle, and throws an exception if it fails
    val course: Course =
        savedStateHandle.get<String>(CourseReviewActivity.EXTRA_COURSE_IDENTIFIER)
            ?.let { course: String ->
                Json.decodeFromString(course)
            } ?: throw IllegalArgumentException("Cannot review a null course")

    /**
     * @return the value of the current rating
     */
    fun getRating(): ReviewRating? = rating.value

    /**
     * @return the value of the current title
     */
    fun getTitle(): String? = title.value

    /**
     * @return the value of the current comment
     */
    fun getComment(): String? = comment.value

    /**
     * @return the date of publication of the review
     */
    fun getDate(): LocalDate? = date

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
     * Set the date of publication of the comment
     * @param date: date of the publication to be set
     */
    fun setDate(date: LocalDate?) {
        this.date = date
    }

    /**
     * Builds the review from user's input. If no date was specified, today's
     * one is used by default
     *
     * @return the CourseReview, if every field is filled, or null otherwise
     */
    fun review(): CourseReview? {
        val rating = rating.value
        val title = title.value
        val comment = comment.value
        val date = date ?: LocalDate.now()

        return if (rating != null && title != null && comment != null) {
            CourseReview.Builder()
                .setRating(rating)
                .setTitle(title)
                .setComment(comment)
                .setDate(date)
                .build()
        } else null
    }
}