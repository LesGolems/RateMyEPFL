package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.review.CourseReview

interface CourseReviewDatabase {
    fun getReviews(): List<CourseReview>
    fun addReview(review: CourseReview)
}