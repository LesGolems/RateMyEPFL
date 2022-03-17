package com.github.sdp.ratemyepfl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.database.CourseDatabase
import com.github.sdp.ratemyepfl.model.items.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * View model of the courseList activity. Bridge between the activity
 * and the database
 * NB: Change the database into repository when implemented
 */
@HiltViewModel
class CourseListViewModel @Inject constructor(val database: CourseDatabase) : ViewModel() {

    private val coursesLiveData = database.getCourses()

    fun getCourses(): LiveData<List<Course>> {
        return coursesLiveData
    }
}