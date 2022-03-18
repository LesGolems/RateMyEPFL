package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.ReviewsRepositoryInterface
import com.github.sdp.ratemyepfl.model.review.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseReviewListViewModel @Inject constructor(private val reviewsRepository: ReviewsRepositoryInterface) : ViewModel() {

    private val reviewsLiveData = MutableLiveData<List<Review?>>()

    init {
        updateReviewsList()
    }

    private fun updateReviewsList() {
        viewModelScope.launch {
            reviewsLiveData.value = reviewsRepository.get()
        }
    }

    fun getReviews(): LiveData<List<Review?>> {
        return reviewsLiveData
    }

}