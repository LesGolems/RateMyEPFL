package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.CourseReview

interface CourseDatabase {
    fun getReviews(): List<CourseReview>
    fun getCourses(): List<Course>
}