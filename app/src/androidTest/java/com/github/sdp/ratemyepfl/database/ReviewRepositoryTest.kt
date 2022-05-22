package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.ReviewRepositoryImpl.Companion.toReview
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.model.time.Date
import com.github.sdp.ratemyepfl.model.time.Date.Companion.toDate
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
        Date(2022, 4, 8),
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
            LocalDate.of(2022, 4, 8).toDate()
        )
        runTest {
            val id = reviewRepo.add(testReviewNoId).await()
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
    fun addWithIdRegisterWithTheGivenId() = runTest {
        val id = "id"
        reviewRepo.addWithId(testReview, id).await()

        val fetched = reviewRepo.getById(id)
        assertEquals(testReview, fetched)

        reviewRepo.remove(id).await()
    }

    @Test
    fun addWithoutIdUsesARandomOne() = runTest {
        val title = "addRandom"
        val review = testReview.copy(title = title)
        val id = reviewRepo.add(review).await()

        val fetched = reviewRepo.getReviews()
            .first { it.title == title }
        assertEquals(review, fetched)
        reviewRepo.remove(id).await()
    }

    @Test
    fun addUpVoteOnceWorks() = runTest {
        val id = "fakeIdAdd"

        reviewRepo.addWithId(
            testReview.copy(likers = listOf("1")), id
        ).await()

        val uid = "uid"
        reviewRepo.addUpVote(id, uid)

        val updated = reviewRepo.getById(id)!!
        assertEquals(listOf("1", uid), updated.likers)
        reviewRepo.remove(id).await()
        clean()
    }


    @Test
    fun addUpVoteTwiceOnlyAddsItOnceWorks() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"

        reviewRepo.addWithId(testReview.copy(likers = listOf(uid)), id).await()

        reviewRepo.addUpVote(id, uid)

        val updated = reviewRepo.getById(id)!!
        assertEquals(listOf(uid), updated.likers)
        reviewRepo.remove(id).await()
        clean()
    }

    @Test
    fun addDownVoteOnceWorks() = runTest {
        val id = "fakeIdAdd"
        reviewRepo.addWithId(testReview.copy(dislikers = listOf("1")), id).await()

        val uid = "uid"
        reviewRepo.addDownVote(id, uid)

        val updated = reviewRepo.getById(id)!!
        assertEquals(listOf("1", uid), updated.dislikers)
        reviewRepo.remove(id).await()
        clean()
    }


    @Test
    fun addDownVoteTwiceOnlyAddsItOnceWorks() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"

        reviewRepo.addWithId(testReview.copy(dislikers = listOf(uid)), id).await()

        reviewRepo.addDownVote(id, uid)

        val updated = reviewRepo.getById(id)!!
        assertEquals(listOf(uid), updated.dislikers)
        reviewRepo.remove(id).await()
        clean()
    }

    @Test
    fun removeUpVoteRemoveExistingUpVote() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"

        reviewRepo.addWithId(testReview.copy(likers = listOf("uid")), id).await()

        reviewRepo.removeUpVote(id, uid)

        val updated = reviewRepo.getById(id)!!
        assertEquals(listOf<String>(), updated.likers)
        reviewRepo.remove(id).await()
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
            Date(2022, 4, 8),
            likers = listOf(uid)
        ).withId(id)

        reviewRepo.addWithId(review, id).await()

        reviewRepo.removeUpVote(id, "someNonExistingId")

        val updated = reviewRepo.getById(id)!!
        assertEquals(listOf(uid), updated.likers)
        reviewRepo.remove(id).await()
        clean()
    }

    @Test
    fun removeDownVoteRemoveExistingUpVote() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"

        reviewRepo.addWithId(testReview.copy(dislikers = listOf(uid)), id).await()

        reviewRepo.removeDownVote(id, uid)

        val updated = reviewRepo.getById(id)!!
        assertEquals(listOf<String>(), updated.dislikers)
        reviewRepo.remove(id).await()
        clean()
    }

    @Test
    fun removeNonExistingDownVoteDoesNotChange() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"

        reviewRepo.addWithId(testReview.copy(dislikers = listOf(uid)), id).await()

        reviewRepo.removeDownVote(id, "someNonExistingId")

        val updated = reviewRepo.getById(id)!!
        assertEquals(listOf(uid), updated.dislikers)
        reviewRepo.remove(id).await()
        clean()
    }
}