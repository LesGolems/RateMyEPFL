package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.ReviewRating

interface ItemsRepositoryInterface {
    suspend fun getClassrooms(): List<Classroom?>
    suspend fun getCourses(): List<Course?>
    suspend fun getById(id: String): Reviewable?
    suspend fun getByIdCourses(id: String): Course?
    suspend fun getByIdClassrooms(id: String): Classroom?
    fun updateRating(rating: ReviewRating, item: Reviewable)
}