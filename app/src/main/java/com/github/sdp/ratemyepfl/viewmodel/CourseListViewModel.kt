package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.placeholder.CoursesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * View model of the courseList activity. Bridge between the activity
 * and the database
 */
@HiltViewModel
class CourseListViewModel @Inject constructor(private val courseRepository: CoursesRepository) : ViewModel() {

    suspend fun getCourses(): List<Course> = courseRepository.get()
}