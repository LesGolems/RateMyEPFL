package com.github.sdp.ratemyepfl.database

import androidx.lifecycle.LiveData
import com.github.sdp.ratemyepfl.model.items.Course

interface CourseDatabase {
    fun getCourses(): LiveData<List<Course>>
}