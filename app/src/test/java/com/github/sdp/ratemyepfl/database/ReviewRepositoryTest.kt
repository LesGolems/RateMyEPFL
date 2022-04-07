package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.ReviewRepository.Companion.toReview
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import java.time.LocalDate

class ReviewRepositoryTest {

    @Test
    fun toItemReturnsAReviewForCompleteSnapshot() {
        val fakeId = "fake"
        val fakeRating = "AVERAGE"
        val fakeTitle = "My title"
        val fakeComment = "My comment"
        val fakeReviewableId = "CS-666"
        val fakeDate = "2022-04-05"
        val fakeLikers = listOf("liker Id")
        val fakeDislikers = listOf("disliker Id")

        val snapshot = Mockito.mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id)
            .thenReturn(fakeId)
        Mockito.`when`(snapshot.getString(ReviewRepository.RATING_FIELD_NAME))
            .thenReturn(fakeRating)
        Mockito.`when`(snapshot.getString(ReviewRepository.TITLE_FIELD_NAME))
            .thenReturn(fakeTitle)
        Mockito.`when`(snapshot.getString(ReviewRepository.COMMENT_FIELD_NAME))
            .thenReturn(fakeComment)
        Mockito.`when`(snapshot.getString(ReviewRepository.REVIEWABLE_ID_FIELD_NAME))
            .thenReturn(fakeReviewableId)
        Mockito.`when`(snapshot.getString(ReviewRepository.DATE_FIELD_NAME))
            .thenReturn(fakeDate)
        Mockito.`when`(snapshot.get(ReviewRepository.LIKERS_FIELD_NAME))
            .thenReturn(fakeLikers)
        Mockito.`when`(snapshot.get(ReviewRepository.DISLIKERS_FIELD_NAME))
            .thenReturn(fakeDislikers)

        val review: Review? = snapshot.toReview()
        val fakeReview = Review.Builder()
            .setId(fakeId)
            .setRating(ReviewRating.valueOf(fakeRating))
            .setTitle(fakeTitle)
            .setComment(fakeComment)
            .setReviewableID(fakeReviewableId)
            .setDate(LocalDate.parse(fakeDate))
            .setLikers(fakeLikers)
            .setDislikers(fakeDislikers)
            .build()

        assertEquals(review, fakeReview)
    }

    @Test
    fun toItemReturnsNullForIncompleteSnapshot() {
        val fakeId = null
        val fakeRating = null
        val fakeTitle = "My title"
        val fakeComment = "My comment"
        val fakeReviewableId = null
        val fakeDate = "2022-04-05"
        val fakeLikers = listOf("liker Id")
        val fakeDislikers = listOf("disliker Id")

        val snapshot = Mockito.mock(DocumentSnapshot::class.java)
        Mockito.`when`(snapshot.id)
            .thenReturn(fakeId)
        Mockito.`when`(snapshot.getString(ReviewRepository.RATING_FIELD_NAME))
            .thenReturn(fakeRating)
        Mockito.`when`(snapshot.getString(ReviewRepository.TITLE_FIELD_NAME))
            .thenReturn(fakeTitle)
        Mockito.`when`(snapshot.getString(ReviewRepository.COMMENT_FIELD_NAME))
            .thenReturn(fakeComment)
        Mockito.`when`(snapshot.getString(ReviewRepository.REVIEWABLE_ID_FIELD_NAME))
            .thenReturn(fakeReviewableId)
        Mockito.`when`(snapshot.getString(ReviewRepository.DATE_FIELD_NAME))
            .thenReturn(fakeDate)
        Mockito.`when`(snapshot.get(ReviewRepository.LIKERS_FIELD_NAME))
            .thenReturn(fakeLikers)
        Mockito.`when`(snapshot.get(ReviewRepository.DISLIKERS_FIELD_NAME))
            .thenReturn(fakeDislikers)

        val review: Review? = snapshot.toReview()
        assertEquals(null, review)
    }
}