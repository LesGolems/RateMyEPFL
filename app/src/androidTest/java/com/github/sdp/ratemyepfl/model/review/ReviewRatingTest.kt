package com.github.sdp.ratemyepfl.model.review

import org.junit.Assert.assertEquals
import org.junit.Test

class ReviewRatingTest {
    @Test
    fun reviewRatingGivesTheRightValues() {
        val terrible = ReviewRating.TERRIBLE
        assertEquals(ReviewRating.TERRIBLE_RATING, terrible.rating)
        val poor = ReviewRating.POOR
        assertEquals(ReviewRating.POOR_RATING, poor.rating)
        val average = ReviewRating.AVERAGE
        assertEquals(ReviewRating.AVERAGE_RATING, average.rating)
        val good = ReviewRating.GOOD
        assertEquals(ReviewRating.GOOD_RATING, good.rating)
        val excellent = ReviewRating.EXCELLENT
        assertEquals(ReviewRating.EXCELLENT_RATING, excellent.rating)

    }

    @Test
    fun fromValueReturnsCorrectValues() {
        val terrible = ReviewRating.TERRIBLE
        assertEquals(ReviewRating.fromValue(1.2f), terrible)
        val poor = ReviewRating.POOR
        assertEquals(ReviewRating.fromValue(2.49f), poor)
        val average = ReviewRating.AVERAGE
        assertEquals(ReviewRating.fromValue(3.0f), average)
        val good = ReviewRating.GOOD
        assertEquals(ReviewRating.fromValue(3.51f), good)
        val excellent = ReviewRating.EXCELLENT
        assertEquals(ReviewRating.fromValue(5.25f), excellent)
    }
}