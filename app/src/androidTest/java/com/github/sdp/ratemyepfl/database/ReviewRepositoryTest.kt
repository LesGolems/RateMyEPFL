package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.ReviewRepository.Companion.toReview
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.mockito.Mockito
import java.time.LocalDate
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ReviewRepositoryTest {
    val testReview = Review(
        "Fake id",
        ReviewRating.EXCELLENT,
        "title",
        "comment",
        "Fake reviewable id",
        LocalDate.of(2022, 4, 8)
    )

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var reviewRepo: ReviewRepository

    @Before
    fun setup() {
        hiltRule.inject()
        reviewRepo.add(testReview)
    }

    @After
    fun clean() {
        reviewRepo.remove(testReview.id)
    }

    @Test
    fun getReviewsWorks() {
        runTest {
            val reviews = reviewRepo.getReviews()
            assertEquals(reviews.size, 1)

            val review = reviews[0]
            assertEquals(review.id, testReview.id)
            assertEquals(review.title, testReview.title)
            assertEquals(review.rating.toString(), testReview.rating.toString())
            assertEquals(review.comment, testReview.comment)
            assertEquals(review.reviewableId, testReview.reviewableId)
            assertEquals(review.date, testReview.date)
        }
    }

    @Test
    fun getReviewByIdWorks() {
        runTest {
            val review = reviewRepo.getReviewById(testReview.id)
            assertNotNull(review)
            assertEquals(review!!.id, testReview.id)
            assertEquals(review.title, testReview.title)
            assertEquals(review.rating.toString(), testReview.rating.toString())
            assertEquals(review.comment, testReview.comment)
            assertEquals(review.reviewableId, testReview.reviewableId)
            assertEquals(review.date, testReview.date)
        }
    }

    @Test
    fun getReviewByReviewableIdWorks() {
        runTest {
            val reviews = reviewRepo.getByReviewableId(testReview.reviewableId)
            assertEquals(reviews.size, 1)
            val review = reviews[0]
            assertEquals(review.id, testReview.id)
            assertEquals(review.title, testReview.title)
            assertEquals(review.rating.toString(), testReview.rating.toString())
            assertEquals(review.comment, testReview.comment)
            assertEquals(review.reviewableId, testReview.reviewableId)
            assertEquals(review.date, testReview.date)
        }
    }

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