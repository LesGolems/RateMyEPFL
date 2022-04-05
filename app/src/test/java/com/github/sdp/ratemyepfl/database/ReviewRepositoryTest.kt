package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.ReviewRepository.Companion.toReview
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class ReviewRepositoryTest {

    @Test
    fun toItemReturnsAReviewForCompleteSnapshot() {
        val fakeId = "fake"
        val fakeRating = "AVERAGE"
        val fakeTitle = "My title"
        val fakeComment = "My comment"
        val fakeReviewableId = "CS-666"
        val fakeDate = "2022-04-05"

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

        val review: Review? = snapshot.toReview()
        val fakeReview =
            Review(
                fakeId,
                ReviewRating.valueOf(fakeRating),
                fakeTitle,
                fakeComment,
                fakeReviewableId,
                LocalDate.parse(fakeDate)
            )
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

        val review: Review? = snapshot.toReview()
        assertEquals(null, review)
    }
}