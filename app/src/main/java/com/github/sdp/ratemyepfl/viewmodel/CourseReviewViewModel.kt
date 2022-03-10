package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.items.Course
import com.github.sdp.ratemyepfl.review.CourseReview
import com.github.sdp.ratemyepfl.review.ReviewRating
import java.time.LocalDate

/**
 * View model for the course reviewing feature
 */
class CourseReviewViewModel(val course: Course) : ViewModel() {
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
     * Builds the review from user's input
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

    /**
     * Factory to create a CourseReviewViewModel
     */
    class CourseReviewViewModelFactory(private val course: Course) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            // If model class is correct return them as ViewModel with Value
            if (modelClass.isAssignableFrom(CourseReviewViewModel::class.java)) {
                return CourseReviewViewModel(course) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }

    }
}