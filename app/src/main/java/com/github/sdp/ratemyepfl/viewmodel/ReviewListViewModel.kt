package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.database.CourseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * TO DO: change the database to repository, when available
 */
@HiltViewModel
class ReviewListViewModel(val database: CourseDatabase) : ViewModel() {

    fun getReviews() = database.getReviews()

}