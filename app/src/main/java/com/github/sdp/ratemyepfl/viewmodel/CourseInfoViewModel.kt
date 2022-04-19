package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepository
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseInfoViewModel @Inject constructor(
    private val courseRepo: CourseRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Id
    val id: String =
        savedStateHandle.get<String>(ReviewActivity.EXTRA_ITEM_REVIEWED)!!

    val course = MutableLiveData<Course>()

    init {
        updateCourse()
    }

    fun updateCourse() {
        viewModelScope.launch {
            course.postValue(courseRepo.getCourseById(id))
        }
    }

    fun updateRating(rating: ReviewRating) {
        viewModelScope.launch {
            courseRepo.updateCourseRating(id, rating)
        }
    }
}