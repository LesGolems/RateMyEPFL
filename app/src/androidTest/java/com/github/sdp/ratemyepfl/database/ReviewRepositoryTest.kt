package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.post.PostRepository
import com.github.sdp.ratemyepfl.database.post.ReviewRepositoryImpl
import com.github.sdp.ratemyepfl.database.post.ReviewRepositoryImpl.Companion.toReview
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.time.LocalDate
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ReviewRepositoryTest {
    private val testReview = Review(
        ReviewRating.EXCELLENT,
        "title",
        "comment",
        "Fake reviewable id",
        LocalDate.of(2022, 4, 8)
    ).withId("Fake id")

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var reviewRepo: ReviewRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
        runTest {
            reviewRepo.addWithId(testReview, testReview.getId()).await()
        }
    }

    @After
    fun clean() {
        runTest {
            reviewRepo.remove(testReview.getId()).await()
        }
    }

    @Test
    fun addAndGetIdWorks() {
        val testReviewNoId = Review(
            ReviewRating.EXCELLENT,
            "title",
            "comment",
            "Fake reviewable id",
            LocalDate.of(2022, 4, 8)
        )
        runTest {
            val id = reviewRepo.addAndGetId(testReviewNoId)
            assertNotNull(id)
            reviewRepo.remove(id).await()
        }
    }

    @Test
    fun getReviewsWorks() {
        runTest {
            val review = reviewRepo.getReviews()[0]
            assertEquals(testReview.getId(), review.getId())
            assertEquals(testReview.title, review.title)
            assertEquals(testReview.comment, review.comment)
            assertEquals(testReview.rating.toString(), review.rating.toString())
            assertEquals(testReview.reviewableId, review.reviewableId)
            assertEquals(testReview.date, review.date)
        }
    }

    @Test
    fun getReviewByIdWorks() {
        runTest {
            val review = reviewRepo.getReviewById(testReview.getId())
            assertNotNull(review)
            assertEquals(testReview.getId(), review!!.getId())
            assertEquals(testReview.title, review.title)
            assertEquals(testReview.comment, review.comment)
            assertEquals(testReview.rating.toString(), review.rating.toString())
            assertEquals(testReview.reviewableId, review.reviewableId)
            assertEquals(testReview.date, review.date)
        }
    }

    @Test
    fun getReviewByReviewableIdWorks() {
        runTest {
            val review = reviewRepo.getByReviewableId(testReview.reviewableId)[0]
            assertEquals(testReview.getId(), review.getId())
            assertEquals(testReview.title, review.title)
            assertEquals(testReview.comment, review.comment)
            assertEquals(testReview.rating.toString(), review.rating.toString())
            assertEquals(testReview.reviewableId, review.reviewableId)
            assertEquals(testReview.date, review.date)
        }
    }

    @Test
    fun likersWorks() {
        runTest {
            reviewRepo.addUpVote(testReview.getId(), "Fake uid")
            var review = reviewRepo.getReviewById(testReview.getId())
            assertNotNull(review)
            assertEquals(testReview.getId(), review!!.getId())
            assertEquals(1, review.likers.size)
            assertEquals("Fake uid", review.likers[0])

            reviewRepo.removeUpVote(testReview.getId(), "Fake uid")
            review = reviewRepo.getReviewById(testReview.getId())
            assertNotNull(review)
            assertEquals(testReview.getId(), review!!.getId())
            assertEquals(0, review.likers.size)
        }
    }

    @Test
    fun dislikersWorks() {
        runTest {
            reviewRepo.addDownVote(testReview.getId(), "Fake uid")
            var review: Review = reviewRepo.getReviewById(testReview.getId())!!
            assertNotNull(review)
            assertEquals(testReview.getId(), review.getId())
            assertEquals(1, review.dislikers.size)
            assertEquals("Fake uid", review.dislikers[0])

            reviewRepo.removeDownVote(testReview.getId(), "Fake uid")
            review = reviewRepo.getReviewById(testReview.getId())!!
            assertNotNull(review)
            assertEquals(testReview.getId(), review.getId())
            assertEquals(0, review.dislikers.size)
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
        Mockito.`when`(snapshot.getString(ReviewRepositoryImpl.RATING_FIELD_NAME))
            .thenReturn(fakeRating)
        Mockito.`when`(snapshot.getString(PostRepository.TITLE_FIELD_NAME))
            .thenReturn(fakeTitle)
        Mockito.`when`(snapshot.getString(PostRepository.COMMENT_FIELD_NAME))
            .thenReturn(fakeComment)
        Mockito.`when`(snapshot.getString(ReviewRepositoryImpl.REVIEWABLE_ID_FIELD_NAME))
            .thenReturn(fakeReviewableId)
        Mockito.`when`(snapshot.getString(PostRepository.DATE_FIELD_NAME))
            .thenReturn(fakeDate)
        Mockito.`when`(snapshot.get(PostRepository.LIKERS_FIELD_NAME))
            .thenReturn(fakeLikers)
        Mockito.`when`(snapshot.get(PostRepository.DISLIKERS_FIELD_NAME))
            .thenReturn(fakeDislikers)

        val review: Review? = snapshot.toReview()
        val fakeReview = Review.Builder()
            .setRating(ReviewRating.valueOf(fakeRating))
            .setReviewableID(fakeReviewableId)
            .setTitle(fakeTitle)
            .setComment(fakeComment)
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
        Mockito.`when`(snapshot.getString(ReviewRepositoryImpl.RATING_FIELD_NAME))
            .thenReturn(fakeRating)
        Mockito.`when`(snapshot.getString(PostRepository.TITLE_FIELD_NAME))
            .thenReturn(fakeTitle)
        Mockito.`when`(snapshot.getString(PostRepository.COMMENT_FIELD_NAME))
            .thenReturn(fakeComment)
        Mockito.`when`(snapshot.getString(ReviewRepositoryImpl.REVIEWABLE_ID_FIELD_NAME))
            .thenReturn(fakeReviewableId)
        Mockito.`when`(snapshot.getString(PostRepository.DATE_FIELD_NAME))
            .thenReturn(fakeDate)
        Mockito.`when`(snapshot.get(PostRepository.LIKERS_FIELD_NAME))
            .thenReturn(fakeLikers)
        Mockito.`when`(snapshot.get(PostRepository.DISLIKERS_FIELD_NAME))
            .thenReturn(fakeDislikers)

        val review: Review? = snapshot.toReview()
        assertEquals(null, review)
    }

    @Test
    fun addWithIdRegisterWithTheGivenId() = runTest {
        val id = "id"
        val review = Review(
            ReviewRating.EXCELLENT,
            "title",
            "comment",
            "Fake reviewable id",
            LocalDate.of(2022, 4, 8)
        ).withId(id)

        reviewRepo.addWithId(review, id).await()

        val fetched = reviewRepo.getById(id).toReview()
        assertEquals(review, fetched)
    }

    @Test
    fun addWithoutIdUsesARandomOne() = runTest {
        val title = "addRandom"
        val review = Review(
            ReviewRating.EXCELLENT,
            title,
            "comment",
            "Fake reviewable id",
            LocalDate.of(2022, 4, 8)
        )

        reviewRepo.add(review).await()

        val fetched = reviewRepo.getReviews()
            .first { it.title == title }
        assertEquals(review, fetched)
    }

    @Test
    fun addUpVoteOnceWorks() = runTest {
        val id = "fakeIdAdd"
        val review = Review(
            ReviewRating.EXCELLENT,
            "title",
            "comment",
            "Fake reviewable id",
            LocalDate.of(2022, 4, 8),
            likers = listOf("1")
        ).withId(id)

        reviewRepo.addWithId(review, id).await()

        val uid = "uid"
        reviewRepo.addUpVote(id, uid)

        val updated = reviewRepo.getById(id).toReview()!!
        assertEquals(listOf("1", uid), updated.likers)
        clean()
    }


    @Test
    fun addUpVoteTwiceOnlyAddsItOnceWorks() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"
        val review = Review(
            ReviewRating.EXCELLENT,
            "title",
            "comment",
            "Fake reviewable id",
            LocalDate.of(2022, 4, 8),
            likers = listOf(uid)
        ).withId(id)

        reviewRepo.addWithId(review, id).await()

        reviewRepo.addUpVote(id, uid)

        val updated = reviewRepo.getById(id).toReview()!!
        assertEquals(listOf(uid), updated.likers)
        clean()
    }

    @Test
    fun addDownVoteOnceWorks() = runTest {
        val id = "fakeIdAdd"
        val review = Review(
            ReviewRating.EXCELLENT,
            "title",
            "comment",
            "Fake reviewable id",
            LocalDate.of(2022, 4, 8),
            dislikers = listOf("1")
        ).withId(id)

        reviewRepo.addWithId(review, id).await()

        val uid = "uid"
        reviewRepo.addDownVote(id, uid)

        val updated = reviewRepo.getById(id).toReview()!!
        assertEquals(listOf("1", uid), updated.dislikers)
        clean()
    }


    @Test
    fun addDownVoteTwiceOnlyAddsItOnceWorks() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"
        val review = Review(
            ReviewRating.EXCELLENT,
            "title",
            "comment",
            "Fake reviewable id",
            LocalDate.of(2022, 4, 8),
            dislikers = listOf(uid)
        ).withId(id)

        reviewRepo.addWithId(review, id).await()

        reviewRepo.addDownVote(id, uid)

        val updated = reviewRepo.getById(id).toReview()!!
        assertEquals(listOf(uid), updated.dislikers)
        clean()
    }

    @Test
    fun removeUpVoteRemoveExistingUpVote() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"
        val review = Review(
            ReviewRating.EXCELLENT,
            "title",
            "comment",
            "Fake reviewable id",
            LocalDate.of(2022, 4, 8),
            likers = listOf(uid)
        ).withId(id)

        reviewRepo.addWithId(review, id).await()

        reviewRepo.removeUpVote(id, uid)

        val updated = reviewRepo.getById(id).toReview()!!
        assertEquals(listOf<String>(), updated.likers)
        clean()
    }

    @Test
    fun removeNonExistingUpVoteDoesNotChange() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"
        val review = Review(
            ReviewRating.EXCELLENT,
            "title",
            "comment",
            "Fake reviewable id",
            LocalDate.of(2022, 4, 8),
            likers = listOf(uid)
        ).withId(id)

        reviewRepo.addWithId(review, id).await()

        reviewRepo.removeUpVote(id, "someNonExistingId")

        val updated = reviewRepo.getById(id).toReview()!!
        assertEquals(listOf(uid), updated.likers)
        clean()
    }

    @Test
    fun removeDownVoteRemoveExistingUpVote() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"
        val review = Review(
            ReviewRating.EXCELLENT,
            "title",
            "comment",
            "Fake reviewable id",
            LocalDate.of(2022, 4, 8),
            dislikers = listOf(uid)
        ).withId(id)

        reviewRepo.addWithId(review, id).await()

        reviewRepo.removeDownVote(id, uid)

        val updated = reviewRepo.getById(id).toReview()!!
        assertEquals(listOf<String>(), updated.dislikers)
        clean()
    }

    @Test
    fun removeNonExistingDownVoteDoesNotChange() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"
        val review = Review(
            ReviewRating.EXCELLENT,
            "title",
            "comment",
            "Fake reviewable id",
            LocalDate.of(2022, 4, 8),
            dislikers = listOf(uid)
        ).withId(id)

        reviewRepo.addWithId(review, id).await()

        reviewRepo.removeDownVote(id, "someNonExistingId")

        val updated = reviewRepo.getById(id).toReview()!!
        assertEquals(listOf(uid), updated.dislikers)
        clean()
    }
}