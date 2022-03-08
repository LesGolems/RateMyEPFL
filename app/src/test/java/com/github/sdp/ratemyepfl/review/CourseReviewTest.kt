package com.github.sdp.ratemyepfl.review

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class CourseReviewTest {

    @Test
    fun test() {
        val rate: ReviewRating = ReviewRating.EXCELLENT
        println(rate)
    }

    @Test
    fun builderThrowsExceptionIfRateIsNull() {
        val builder = CourseReview.Builder()
            .setComment("Hello")
            .setTitle("Hello")
            .setDate(LocalDate.now())

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsExceptionIfTitleIsNull() {
        val builder = CourseReview.Builder()
            .setComment("Hello")
            .setRate(ReviewRating.AVERAGE)
            .setDate(LocalDate.now())

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsExceptionIfCommentIsNull() {
        val builder = CourseReview.Builder()
            .setRate(ReviewRating.AVERAGE)
            .setTitle("Hello")
            .setDate(LocalDate.now())

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderSetRateCorrectly() {
        val rating = ReviewRating.TERRIBLE
        val builder: CourseReview = CourseReview.Builder()
            .setRate(rating)
            .setComment("My comment")
            .setTitle("My title")
            .setDate(LocalDate.now())
            .build()

        assertEquals(rating, builder.rating)
    }
    @Test
    fun builderSetTitleCorrectly() {
        val title: String = "My title"
        val builder: CourseReview = CourseReview.Builder()
            .setRate(ReviewRating.TERRIBLE)
            .setComment("My comment")
            .setTitle(title)
            .setDate(LocalDate.now())
            .build()

        assertEquals(title, builder.title)
    }

    @Test
    fun builderSetCommentCorrectly() {
        val comment = "My comment"
        val builder: CourseReview = CourseReview.Builder()
            .setRate(ReviewRating.TERRIBLE)
            .setComment(comment)
            .setTitle("My title")
            .setDate(LocalDate.now())
            .build()

        assertEquals(comment, builder.comment)
    }

    @Test
    fun builderSetDateCorrectly() {
        val rate = ReviewRating.GOOD
        val title = "My title"
        val comment = "My comment"
        val date = LocalDate.of(2022, 3, 8)

        val review = CourseReview.Builder()
            .setRate(rate)
            .setTitle(title)
            .setComment(comment)
            .setDate(date)
            .build()

        assertEquals(date, review.date)
    }
}