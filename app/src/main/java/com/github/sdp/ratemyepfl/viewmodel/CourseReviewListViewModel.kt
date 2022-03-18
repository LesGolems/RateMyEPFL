package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseReviewListViewModel @Inject constructor(private val reviewsRepository: CoursesReviewsRepositoryInterface) : ViewModel() {

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