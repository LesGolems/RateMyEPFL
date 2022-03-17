package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.database.CourseDatabase
import com.github.sdp.ratemyepfl.database.CourseReviewDatabase
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.CourseReview
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * TO DO: change the database to repository, when available
 */
@HiltViewModel
class CourseReviewListViewModel @Inject constructor(database: CourseReviewDatabase) : ViewModel() {
    val database: LiveData<CourseReviewDatabase> = MutableLiveData(database)

    fun getReviews() = database.value?.getReviews()

    fun addReview(course: Course, review: CourseReview) {
        database.value?.addReview(review)
    }
}