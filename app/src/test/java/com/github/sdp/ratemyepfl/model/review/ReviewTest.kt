package com.github.sdp.ratemyepfl.model.review

import com.github.sdp.ratemyepfl.model.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ReviewTest {

    @Test
    fun test() {
        val rate: ReviewRating = ReviewRating.EXCELLENT
        println(rate)
    }

    @Test
    fun builderSetRateCorrectly() {
        val rating = ReviewRating.TERRIBLE
        val builder: Review = Review.Builder()
            .setRating(rating)
            .setReviewableID("ID")
            .setComment("My comment")
            .setTitle("My title")
            .setDate(DateTime.now())
            .build()

        assertEquals(rating, builder.rating)
    }


    @Test
    fun builderThrowsExceptionIfRateIsNull() {
        val builder = Review.Builder()
            .setReviewableID("ID")
            .setComment("Hello")
            .setTitle("Hello")
            .setDate(DateTime.now())
        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsExceptionIfTitleIsNull() {
        val builder = Review.Builder()
            .setRating(ReviewRating.AVERAGE)
            .setReviewableID("ID")
            .setComment("Hello")
            .setDate(DateTime.now())
        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsExceptionIfCommentIsNull() {
        val builder = Review.Builder()
            .setRating(ReviewRating.AVERAGE)
            .setReviewableID("ID")
            .setTitle("Hello")
            .setDate(DateTime.now())

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsExceptionIfReviewableIdIsNull() {
        val builder = Review.Builder()
            .setRating(ReviewRating.AVERAGE)
            .setTitle("Hello")
            .setComment("Hello")
            .setDate(DateTime.now())

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsExceptionIfDateIsNull() {
        val builder = Review.Builder()
            .setRating(ReviewRating.AVERAGE)
            .setReviewableID("ID")
            .setTitle("Hello")
            .setComment("Hello")

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderSetTitleCorrectly() {
        val title = "My title"
        val builder: Review = Review.Builder()
            .setRating(ReviewRating.TERRIBLE)
            .setReviewableID("ID")
            .setComment("My comment")
            .setTitle(title)
            .setDate(DateTime.now())
            .build()

        assertEquals(title, builder.title)
    }

    @Test
    fun builderSetCommentCorrectly() {
        val comment = "My comment"
        val builder: Review = Review.Builder()
            .setRating(ReviewRating.TERRIBLE)
            .setReviewableID("ID")
            .setComment(comment)
            .setTitle("My title")
            .setDate(DateTime.now())
            .build()

        assertEquals(comment, builder.comment)
    }

    @Test
    fun builderSetDateCorrectly() {
        val rate = ReviewRating.GOOD
        val title = "My title"
        val comment = "My comment"
        val date = DateTime(2022, 3, 8, 0, 0)

        val review = Review.Builder()
            .setRating(rate)
            .setReviewableID("ID")
            .setTitle(title)
            .setComment(comment)
            .setDate(date)
            .build()

        assertEquals(date, review.date)
    }

    @Test
    fun builderSetReviewableIdCorrectly() {
        val rate = ReviewRating.GOOD
        val title = "My title"
        val comment = "My comment"
        val reviewableId = "Id"
        val date = DateTime(2022, 3, 8, 0, 0)

        val review = Review.Builder()
            .setRating(rate)
            .setReviewableID(reviewableId)
            .setTitle(title)
            .setComment(comment)
            .setDate(date)
            .build()

        assertEquals(reviewableId, review.reviewableId)
    }
}