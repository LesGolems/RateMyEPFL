package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.model.review.CourseReview
import com.github.sdp.ratemyepfl.placeholder.CoursesReviewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewListViewModel @Inject constructor(private val reviewsRepository: CoursesReviewsRepository) : ViewModel() {

    fun getReviews(): List<CourseReview?> {
        var reviewsList: List<CourseReview?> = emptyList()
        viewModelScope.launch {
            reviewsList = reviewsRepository.get()
        }
        return reviewsList
    }

}