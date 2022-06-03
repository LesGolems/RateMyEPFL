package com.github.sdp.ratemyepfl.backend.database.firebase

import com.github.sdp.ratemyepfl.backend.database.firebase.post.ReviewRepositoryImpl
import com.github.sdp.ratemyepfl.model.post.Review
import com.github.sdp.ratemyepfl.model.post.ReviewRating
import com.github.sdp.ratemyepfl.model.time.DateTime
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ReviewRepositoryTest {
    private val testReview = Review(
        "",
        ReviewRating.EXCELLENT,
        "title",
        "comment",
        "Fake reviewable id",
        DateTime(2022, 4, 8, 0, 0),
    ).withId("Fake id")

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var reviewRepo: ReviewRepositoryImpl

    @Before
    fun setup() {
        hiltRule.inject()
        runTest {
            reviewRepo.addWithId(testReview, testReview.getId()).collect()
        }
    }

    @After
    fun clean() {
        runTest {
            reviewRepo.remove(testReview.getId()).last()
        }
    }

    @Test
    fun addAndGetIdWorks() {
        val testReviewNoId = Review(
            "",
            ReviewRating.EXCELLENT,
            "title",
            "comment",
            "Fake reviewable id",
            DateTime(2022, 4, 8, 0, 0)
        )
        runTest {
            val id = reviewRepo.add(testReviewNoId).last()
            assertNotNull(id)
            reviewRepo.remove(id).last()
        }
    }

    @Test
    fun getReviewsWorks() {
        runTest {
            val review = reviewRepo.get().last()[0]
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
            val review = reviewRepo.getByReviewableId(testReview.reviewableId).last()[0]
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
        reviewRepo.addWithId(testReview, id).collect()

        val fetched = reviewRepo.getById(id).last()
        assertEquals(testReview, fetched)

        reviewRepo.remove(id).last()
    }

    @Test
    fun addWithoutIdUsesARandomOne() = runTest {
        val title = "addRandom"
        val review = testReview.copy(title = title)
        val id = reviewRepo.add(review).last()

        val fetched = reviewRepo.get(10L)
            .last()
            .first { it.title == title }
        assertEquals(review, fetched)
        reviewRepo.remove(id).last()
    }

    @Test
    fun addUpVoteOnceWorks() = runTest {
        val id = "fakeIdAdd"

        reviewRepo.addWithId(
            testReview.copy(likers = listOf("1")), id
        ).last()

        val uid = "uid"
        reviewRepo.addUpVote(id, uid)

        val updated = reviewRepo.getById(id).last()
        assertEquals(listOf("1", uid), updated.likers)
        reviewRepo.remove(id).last()
        clean()
    }

    @Test
    fun addUpVoteTwiceOnlyAddsItOnceWorks() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"

        reviewRepo.addWithId(testReview.copy(likers = listOf(uid)), id).collect()

        reviewRepo.addUpVote(id, uid)

        val updated = reviewRepo.getById(id).last()
        assertEquals(listOf(uid), updated.likers)
        reviewRepo.remove(id).last()
        clean()
    }

    @Test
    fun addDownVoteOnceWorks() = runTest {
        val id = "fakeIdAdd"
        reviewRepo.addWithId(testReview.copy(dislikers = listOf("1")), id).collect()

        val uid = "uid"
        reviewRepo.addDownVote(id, uid)

        val updated = reviewRepo.getById(id).last()
        assertEquals(listOf("1", uid), updated.dislikers)
        reviewRepo.remove(id).last()
        clean()
    }

    @Test
    fun addDownVoteTwiceOnlyAddsItOnceWorks() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"

        reviewRepo.addWithId(testReview.copy(dislikers = listOf(uid)), id).collect()

        reviewRepo.addDownVote(id, uid)

        val updated = reviewRepo.getById(id).last()
        assertEquals(listOf(uid), updated.dislikers)
        reviewRepo.remove(id).last()
        clean()
    }

    @Test
    fun removeUpVoteRemoveExistingUpVote() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"

        reviewRepo.addWithId(testReview.copy(likers = listOf("uid")), id).collect()

        reviewRepo.removeUpVote(id, uid)

        val updated = reviewRepo.getById(id).last()
        assertEquals(listOf<String>(), updated.likers)
        reviewRepo.remove(id).last()
        clean()
    }

    @Test
    fun removeNonExistingUpVoteDoesNotChange() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"
        val review = Review(
            "",
            ReviewRating.EXCELLENT,
            "title",
            "comment",
            "Fake reviewable id",
            DateTime.DEFAULT_DATE_TIME,
            likers = listOf(uid)
        ).withId(id)

        reviewRepo.addWithId(review, id).collect()

        reviewRepo.removeUpVote(id, "someNonExistingId")

        val updated = reviewRepo.getById(id).last()
        assertEquals(listOf(uid), updated.likers)
        reviewRepo.remove(id).last()
        clean()
    }

    @Test
    fun removeDownVoteRemoveExistingUpVote() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"

        reviewRepo.addWithId(testReview.copy(dislikers = listOf(uid)), id).collect()

        reviewRepo.removeDownVote(id, uid)

        val updated = reviewRepo.getById(id).last()
        assertEquals(listOf<String>(), updated.dislikers)
        reviewRepo.remove(id).last()
        clean()
    }

    @Test
    fun removeNonExistingDownVoteDoesNotChange() = runTest {
        val uid = "uid"
        val id = "fakeIdAdd"

        reviewRepo.addWithId(testReview.copy(dislikers = listOf(uid)), id).collect()

        reviewRepo.removeDownVote(id, "someNonExistingId")

        val updated = reviewRepo.getById(id).last()
        assertEquals(listOf(uid), updated.dislikers)
        reviewRepo.remove(id).last()
        clean()
    }
}