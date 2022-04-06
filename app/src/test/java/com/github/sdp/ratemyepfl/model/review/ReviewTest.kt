package com.github.sdp.ratemyepfl.model.review

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDate

class ReviewTest {
    val EXPECTED_REVIEW =
        Review("Fake", ReviewRating.EXCELLENT, "My title", "My comment", "ID", LocalDate.of(2020, 3, 8))
    val EXPECTED_JSON =
        "{\"id\":\"Fake\",\"rating\":\"EXCELLENT\",\"title\":\"My title\",\"comment\":\"My comment\",\"reviewableId\":\"ID\",\"date\":\"2020-03-08\"}"

    @Test
    fun test() {
        val rate: ReviewRating = ReviewRating.EXCELLENT
        println(rate)
    }

    @Test
    fun builderThrowsExceptionIfIdIsNull() {
        val builder = Review.Builder()
            .setComment("Hello")
            .setRating(ReviewRating.AVERAGE)
            .setTitle("Hello")
            .setReviewableID("ID")
            .setDate(LocalDate.now())

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsExceptionIfRateIsNull() {
        val builder = Review.Builder()
            .setId("Fake")
            .setComment("Hello")
            .setTitle("Hello")
            .setReviewableID("ID")
            .setDate(LocalDate.now())

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsExceptionIfTitleIsNull() {
        val builder = Review.Builder()
            .setId("Fake")
            .setComment("Hello")
            .setRating(ReviewRating.AVERAGE)
            .setReviewableID("ID")
            .setDate(LocalDate.now())

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsExceptionIfCommentIsNull() {
        val builder = Review.Builder()
            .setId("Fake")
            .setRating(ReviewRating.AVERAGE)
            .setTitle("Hello")
            .setReviewableID("ID")
            .setDate(LocalDate.now())

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsExceptionIfReviewableIdIsNull() {
        val builder = Review.Builder()
            .setId("Fake")
            .setRating(ReviewRating.AVERAGE)
            .setTitle("Hello")
            .setComment("Hello")
            .setDate(LocalDate.now())

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsExceptionIfDateIsNull() {
        val builder = Review.Builder()
            .setId("Fake")
            .setRating(ReviewRating.AVERAGE)
            .setTitle("Hello")
            .setComment("Hello")
            .setReviewableID("ID")

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderSetRateCorrectly() {
        val rating = ReviewRating.TERRIBLE
        val builder: Review = Review.Builder()
            .setId("Fake")
            .setRating(rating)
            .setComment("My comment")
            .setTitle("My title")
            .setReviewableID("ID")
            .setDate(LocalDate.now())
            .build()

        assertEquals(rating, builder.rating)
    }

    @Test
    fun builderSetTitleCorrectly() {
        val title = "My title"
        val builder: Review = Review.Builder()
            .setId("Fake")
            .setRating(ReviewRating.TERRIBLE)
            .setComment("My comment")
            .setTitle(title)
            .setReviewableID("ID")
            .setDate(LocalDate.now())
            .build()

        assertEquals(title, builder.title)
    }

    @Test
    fun builderSetCommentCorrectly() {
        val comment = "My comment"
        val builder: Review = Review.Builder()
            .setId("Fake")
            .setRating(ReviewRating.TERRIBLE)
            .setComment(comment)
            .setTitle("My title")
            .setReviewableID("ID")
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

        val review = Review.Builder()
            .setId("Fake")
            .setRating(rate)
            .setTitle(title)
            .setComment(comment)
            .setReviewableID("ID")
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
        val date = LocalDate.of(2022, 3, 8)

        val review = Review.Builder()
            .setId("Fake")
            .setRating(rate)
            .setTitle(title)
            .setComment(comment)
            .setReviewableID(reviewableId)
            .setDate(date)
            .build()

        assertEquals(reviewableId, review.reviewableId)
    }

    @Test
    fun serializationIsConsistent() {
        val review: Review = Review.Builder()
            .setId("Fake")
            .setRating(ReviewRating.EXCELLENT)
            .setComment("My comment")
            .setTitle("My title")
            .setReviewableID("ID")
            .setDate(LocalDate.of(2020, 3, 8))
            .build()

        val serializedReview = review.serialize()
        val deserializedReview = Review.deserialize(serializedReview)

        assertEquals(review, deserializedReview)
    }

    @Test
    fun serializeWorks() {
        val serializedReview = EXPECTED_REVIEW.serialize()
        assertEquals(serializedReview, EXPECTED_JSON)
    }
}