package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable

interface ItemsRepositoryInterface {
    suspend fun getClassrooms(): List<Classroom?>
    suspend fun getCourses(): List<Course?>
    suspend fun getById(id: String): Reviewable?
}