package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val reviewsLiveData = MutableLiveData<List<Course>>()

    init {
        updateCoursesList()
    }

    private fun updateCoursesList() {
        viewModelScope.launch {
            reviewsLiveData.value = courseRepository.get()
        }
    }

    fun getCourses(): LiveData<List<Course>> {
        return reviewsLiveData
    }
}