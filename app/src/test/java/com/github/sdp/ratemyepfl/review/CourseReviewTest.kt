package com.github.sdp.ratemyepfl.review

import org.junit.Test

class CourseReviewTest {

    @Test
    fun test() {
        val rate: ReviewRating = ReviewRating.EXCELLENT
        println(rate)
    }

    @Test
    fun builderSetRateCorrectly() {
        var builder: CourseReview = CourseReview.Builder()
            .setRate(ReviewRating.EXCELLENT)
            .setComment("My comment")
            .setTitle("My title")
            .build()
    }
}