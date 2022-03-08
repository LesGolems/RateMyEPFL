package com.github.sdp.ratemyepfl.review

import org.junit.Assert.*
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
}