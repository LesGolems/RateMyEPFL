package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.placeholder.CoursesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model of the courseList activity. Bridge between the activity
 * and the database
 */
@HiltViewModel
class CourseListViewModel @Inject constructor(private val courseRepository: CoursesRepository) : ViewModel() {

    fun getCourses(): List<Course> {
        var reviewsList: List<Course> = emptyList()
        viewModelScope.launch {
            reviewsList = courseRepository.get()
        }
        return reviewsList
    }
}