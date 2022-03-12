package com.github.sdp.ratemyepfl.model.review

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
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
            .setRating(ReviewRating.AVERAGE)
            .setDate(LocalDate.now())

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsExceptionIfCommentIsNull() {
        val builder = CourseReview.Builder()
            .setRating(ReviewRating.AVERAGE)
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
            .setRating(rating)
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
            .setRating(ReviewRating.TERRIBLE)
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
            .setRating(ReviewRating.TERRIBLE)
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
            .setRating(rate)
            .setTitle(title)
            .setComment(comment)
            .setDate(date)
            .build()

        assertEquals(date, review.date)
    }

    @Test
    fun serializationIsConsistent() {
        val review: CourseReview = CourseReview.Builder()
            .setRating(ReviewRating.EXCELLENT)
            .setComment("My comment")
            .setTitle("My title")
            .setDate(LocalDate.of(2020, 3, 8))
            .build()

        val serializedReview = CourseReview.serialize(review)
        val deserializedReview = CourseReview.deserialize(serializedReview)

        assertEquals(review, deserializedReview)
    }
}