package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.items.Course
import com.github.sdp.ratemyepfl.review.CourseReview
import com.github.sdp.ratemyepfl.review.ReviewRating
import java.time.LocalDate

class CourseReviewViewModel(val course: Course) : ViewModel() {
    val rating: MutableLiveData<ReviewRating> = MutableLiveData(null)
    val title: MutableLiveData<String> = MutableLiveData(null)
    val comment: MutableLiveData<String> = MutableLiveData(null)
    private var date: LocalDate? = null

    private fun setRating(rating: ReviewRating?) {
        this.rating.postValue(rating)
    }

    fun setRating(buttonId: Int) {
        setRating(fromIdToRating(buttonId))
    }

    fun setTitle(title: String?) = this.title.postValue(title)

    fun setComment(comment: String?) = this.comment.postValue(comment)


    fun review(): CourseReview? {
        val rating = rating.value
        val title = title.value
        val comment = comment.value
        val date = date ?: LocalDate.now()

        return if (rating != null && title != null && comment != null) {
            CourseReview.Builder()
                .setRate(rating)
                .setTitle(title)
                .setComment(comment)
                .setDate(date)
                .build()
        } else null
    }

    private fun fromIdToRating(id: Int): ReviewRating? = when (id) {
        R.id.courseRatingTerribleRadioButton -> ReviewRating.TERRIBLE
        R.id.courseRatingPoorRadioButton -> ReviewRating.POOR
        R.id.courseRatingAverageRadioButton -> ReviewRating.AVERAGE
        R.id.courseRatingGoodRadioButton -> ReviewRating.GOOD
        R.id.courseRatingExcellentRadioButton -> ReviewRating.EXCELLENT
        else -> null
    }

    class CourseReviewViewModelFactory(val course: Course) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            // If model class is correct return them as ViewModel with Value
            if (modelClass.isAssignableFrom(CourseReviewViewModel::class.java)) {
                return CourseReviewViewModel(course) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }

    }
}