package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.database.CourseRepositoryInterface
import com.github.sdp.ratemyepfl.database.ReviewsRepository
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseInfoViewModel @Inject constructor(
    private val reviewRepo: ReviewsRepository,
    private val courseRepo: CourseRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ReviewViewModel(
    reviewRepo, savedStateHandle
) {
    val course = MutableLiveData<Course>()

    init {
        updateCourse()
    }

    fun updateCourse() {
        viewModelScope.launch {
            course.postValue(courseRepo.getCourseById(id))
        }
    }

    fun updateRating(rating: ReviewRating){
        viewModelScope.launch {
            courseRepo.updateCourseRating(id, rating)
        }
    }
}