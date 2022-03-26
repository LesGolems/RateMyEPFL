package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.ReviewRating

interface ItemsRepositoryInterface {
    suspend fun getClassrooms(): List<Classroom?>
    suspend fun getCourses(): List<Course?>
    suspend fun getRestaurants(): List<Restaurant?>
    suspend fun getById(id: String?): Reviewable?
    suspend fun getByIdCourses(id: String): Course?
    suspend fun getByIdClassrooms(id: String): Classroom?
    suspend fun getByIdRestaurants(id: String): Restaurant?
    fun updateRating(rating: ReviewRating, item: Reviewable)
}
