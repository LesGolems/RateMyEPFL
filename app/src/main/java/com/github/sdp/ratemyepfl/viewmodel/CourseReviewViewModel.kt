package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.*
import com.github.sdp.ratemyepfl.database.ItemsRepositoryInterface
import com.github.sdp.ratemyepfl.database.ReviewsRepositoryInterface
import com.github.sdp.ratemyepfl.model.items.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseReviewViewModel @Inject constructor(
    private val reviewsRepository: ReviewsRepositoryInterface,
    private val itemsRepository: ItemsRepositoryInterface,
    private val savedStateHandle: SavedStateHandle
) : ReviewViewModel(reviewsRepository, itemsRepository, savedStateHandle) {

    // Course
    private val course = MutableLiveData<Course?>()

    init {
        updateCourse()
        updateReviewsList()
    }

    fun updateCourse() {
        viewModelScope.launch {
            course.value = itemsRepository.getByIdCourses(id!!)
        }
    }

    fun getCourse(): LiveData<Course?> {
        return course
    }
}