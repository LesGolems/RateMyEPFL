package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.review.CourseReview

interface CoursesReviewsRepositoryInterface {
    suspend fun get() : List<CourseReview?>
    fun add(value: CourseReview)
}