package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Restaurant

interface ItemsRepositoryInterface {
    suspend fun getClassrooms() : List<Classroom?>
    suspend fun getCourses() : List<Course?>
    suspend fun getRestaurants() : List<Restaurant?>
}