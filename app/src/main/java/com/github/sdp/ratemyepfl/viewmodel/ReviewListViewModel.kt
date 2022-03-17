package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.model.review.CourseReview
import com.github.sdp.ratemyepfl.database.CoursesReviewsRepository
import com.github.sdp.ratemyepfl.database.CoursesReviewsRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewListViewModel @Inject constructor(private val reviewsRepository: CoursesReviewsRepositoryInterface) : ViewModel() {

    private val reviewsLiveData = MutableLiveData<List<CourseReview?>>()

    init {
        updateReviewsList()
    }

    private fun updateReviewsList() {
        viewModelScope.launch {
            reviewsLiveData.value = reviewsRepository.get()
        }
    }

    fun getReviews(): LiveData<List<CourseReview?>> {
        return reviewsLiveData
    }

}