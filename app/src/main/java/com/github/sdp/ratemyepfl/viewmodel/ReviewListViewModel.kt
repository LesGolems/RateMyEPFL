package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.database.CourseDatabase
import com.github.sdp.ratemyepfl.database.CourseReviewDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * TO DO: change the database to repository, when available
 */
@HiltViewModel
class ReviewListViewModel @Inject constructor(val database: CourseReviewDatabase) : ViewModel() {

    fun getReviews() = database.getReviews()

}