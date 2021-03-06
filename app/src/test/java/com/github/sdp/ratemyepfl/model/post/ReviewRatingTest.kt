package com.github.sdp.ratemyepfl.model.post

import org.junit.Assert.assertEquals
import org.junit.Test

class ReviewRatingTest {
    @Test
    fun reviewRatingGivesTheRightValues() {
        val terrible = ReviewRating.TERRIBLE
        assertEquals(TERRIBLE_RATING, terrible.rating)
        val poor = ReviewRating.POOR
        assertEquals(POOR_RATING, poor.rating)
        val average = ReviewRating.AVERAGE
        assertEquals(AVERAGE_RATING, average.rating)
        val good = ReviewRating.GOOD
        assertEquals(GOOD_RATING, good.rating)
        val excellent = ReviewRating.EXCELLENT
        assertEquals(EXCELLENT_RATING, excellent.rating)
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

    @Test
    fun toValueReturnsCorrectValue() {
        val terrible = ReviewRating.TERRIBLE
        assertEquals(terrible.toValue(), 1)
        val poor = ReviewRating.POOR
        assertEquals(poor.toValue(), 2)
        val average = ReviewRating.AVERAGE
        assertEquals(average.toValue(), 3)
        val good = ReviewRating.GOOD
        assertEquals(good.toValue(), 4)
        val excellent = ReviewRating.EXCELLENT
        assertEquals(excellent.toValue(), 5)
    }

    @Test
    fun toStringReturnsCorrectString() {
        val terrible = ReviewRating.TERRIBLE
        assertEquals(terrible.toString(), "Terrible")
        val poor = ReviewRating.POOR
        assertEquals(poor.toString(), "Poor")
        val average = ReviewRating.AVERAGE
        assertEquals(average.toString(), "Average")
        val good = ReviewRating.GOOD
        assertEquals(good.toString(), "Good")
        val excellent = ReviewRating.EXCELLENT
        assertEquals(excellent.toString(), "Excellent")
    }
}