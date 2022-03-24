package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course

interface ItemsRepositoryInterface {
    suspend fun getClassrooms(): List<Classroom?>
    suspend fun getCourses(): List<Course?>
}