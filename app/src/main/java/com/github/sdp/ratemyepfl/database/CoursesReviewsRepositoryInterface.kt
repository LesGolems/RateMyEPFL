package com.github.sdp.ratemyepfl.database

interface CoursesReviewsRepositoryInterface {
    suspend fun get() : List<CourseReview?>
    fun add(value: CourseReview)
}