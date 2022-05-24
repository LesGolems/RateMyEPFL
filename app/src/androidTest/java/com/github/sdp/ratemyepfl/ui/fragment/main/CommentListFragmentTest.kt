package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeCommentRepository
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeReviewRepository
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeUserRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.model.review.Comment
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.time.DateTime
import com.github.sdp.ratemyepfl.ui.adapter.post.PostAdapter
import com.github.sdp.ratemyepfl.utils.RecyclerViewUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@ExperimentalCoroutinesApi
class CommentListFragmentTest {

    @get:Rule
    val hiltAndroidTestRule = HiltAndroidRule(this)

    fun launch() {
        val b = Bundle()
        b.putString(CommentListFragment.EXTRA_SUBJECT_COMMENTED_ID, "fake id")
        HiltUtils.launchFragmentInHiltContainer<CommentListFragment>(b)
    }

    /**
     * Like the ([position]+1)-th comment
     */
    private fun likeComment(position: Int) {
        onView(withId(R.id.commentRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PostAdapter<Review>.PostViewHolder>(
                position,
                RecyclerViewUtils.clickOnViewChild(R.id.likeButton)
            )
        )
    }

    /**
     * Dislike the ([position]+1)-th comment
     */
    private fun dislikeComment(position: Int) {
        onView(withId(R.id.commentRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PostAdapter<Review>.PostViewHolder>(
                position,
                RecyclerViewUtils.clickOnViewChild(R.id.dislikeButton)
            )
        )
    }

    private fun refresh() {
        onView(withId(R.id.commentSwipeRefresh)).perform(ViewActions.swipeDown())
    }

    @Test
    fun addCommentWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        launch()

        onView(withId(R.id.addComment)).perform(typeText("my comment"))
        onView(withId(R.id.addComment)).perform(click())
        onView(withId(R.id.doneButton)).perform(click())
    }

    @Test
    fun addEmptyCommentDoesNotWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        launch()

        onView(withId(R.id.addComment)).perform(click())
        onView(withId(R.id.addComment)).perform(click())
        onView(withId(R.id.doneButton)).perform(click())
    }

    @Test
    fun addAnonCommentWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        launch()

        onView(withId(R.id.addComment)).perform(typeText("my comment"))
        onView(withId(R.id.addComment)).perform(click())
        onView(withId(R.id.addCommentAnonymousSwitch)).perform(click())
        onView(withId(R.id.doneButton)).perform(click())
    }

    @Test
    fun addCommentNotLoggedInDoesNotWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        launch()

        onView(withId(R.id.addComment)).perform(typeText("my comment"))
        onView(withId(R.id.addComment)).perform(click())
        onView(withId(R.id.doneButton)).perform(click())
    }

    @Test
    fun likeThenLikeReview() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        FakeCommentRepository.commentList = listOf(
            Comment.Builder()
                .setSubjectID("id")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .setLikers(
                    listOf(
                        FakeReviewRepository.FAKE_UID_1,
                        FakeReviewRepository.FAKE_UID_2
                    )
                )
                .setDislikers(
                    listOf(
                        FakeReviewRepository.FAKE_UID_3,
                        FakeReviewRepository.FAKE_UID_4
                    )
                )
                .setUid(FakeUserRepository.UID1)
                .build()
        )
        launch()

        likeComment(0)
        refresh()
        likeComment(0)
    }

    @Test
    fun dislikeThenDislikeReview() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        FakeCommentRepository.commentList = listOf(
            Comment.Builder()
                .setSubjectID("id")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .setLikers(
                    listOf(
                        FakeReviewRepository.FAKE_UID_1,
                        FakeReviewRepository.FAKE_UID_2
                    )
                )
                .setDislikers(
                    listOf(
                        FakeReviewRepository.FAKE_UID_3,
                        FakeReviewRepository.FAKE_UID_4
                    )
                )
                .setUid(FakeUserRepository.UID1)
                .build()
        )
        launch()

        dislikeComment(0)
        refresh()
        dislikeComment(0)
    }

    @Test
    fun likeThenDislikeReview() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        FakeCommentRepository.commentList = listOf(
            Comment.Builder()
                .setSubjectID("id")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .setLikers(
                    listOf(
                        FakeReviewRepository.FAKE_UID_1,
                        FakeReviewRepository.FAKE_UID_2
                    )
                )
                .setDislikers(
                    listOf(
                        FakeReviewRepository.FAKE_UID_3,
                        FakeReviewRepository.FAKE_UID_4
                    )
                )
                .setUid(FakeUserRepository.UID1)
                .build()
        )
        launch()

        likeComment(0)
        refresh()
        dislikeComment(0)
    }

    @Test
    fun dislikeThenLikeReview() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        FakeCommentRepository.commentList = listOf(
            Comment.Builder()
                .setSubjectID("id")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .setLikers(
                    listOf(
                        FakeReviewRepository.FAKE_UID_1,
                        FakeReviewRepository.FAKE_UID_2
                    )
                )
                .setDislikers(
                    listOf(
                        FakeReviewRepository.FAKE_UID_3,
                        FakeReviewRepository.FAKE_UID_4
                    )
                )
                .setUid(FakeUserRepository.UID1)
                .build()
        )
        launch()

        dislikeComment(0)
        refresh()
        likeComment(0)
    }

    @Test
    fun likeWhenNotConnected() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        launch()
        likeComment(0)
    }

    @Test
    fun dislikeWhenNotConnected() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        launch()
        dislikeComment(0)
    }

    @Test
    fun deleteButtonRemovesReview() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        FakeCommentRepository.commentList = listOf(
            Comment.Builder()
                .setSubjectID("id")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .setLikers(
                    listOf(
                        FakeReviewRepository.FAKE_UID_1,
                        FakeReviewRepository.FAKE_UID_2
                    )
                )
                .setDislikers(
                    listOf(
                        FakeReviewRepository.FAKE_UID_3,
                        FakeReviewRepository.FAKE_UID_4
                    )
                )
                .setUid(FakeUserRepository.UID1)
                .build()
        )

        launch()

        onView(withId(R.id.commentRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PostAdapter<Review>.PostViewHolder>(
                0,
                RecyclerViewUtils.clickOnViewChild(R.id.deleteButton)
            )
        )

        refresh()

        Assert.assertEquals(listOf<Review>(), FakeCommentRepository.commentList)
    }

    @Test
    fun likeItsOwnReviewThrowsException() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        FakeCommentRepository.commentList = listOf(
            Comment.Builder()
                .setSubjectID("id")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .setLikers(
                    listOf(
                        FakeReviewRepository.FAKE_UID_1,
                        FakeReviewRepository.FAKE_UID_2
                    )
                )
                .setDislikers(
                    listOf(
                        FakeReviewRepository.FAKE_UID_3,
                        FakeReviewRepository.FAKE_UID_4
                    )
                )
                .setUid(FakeConnectedUser.fakeUser1.uid)
                .build()
        )

        launch()

        likeComment(0)
    }

    @Test
    fun dislikeItsOwnReviewThrowsException() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        FakeCommentRepository.commentList = listOf(
            Comment.Builder()
                .setSubjectID("id")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .setLikers(
                    listOf(
                        FakeReviewRepository.FAKE_UID_1,
                        FakeReviewRepository.FAKE_UID_2
                    )
                )
                .setDislikers(
                    listOf(
                        FakeReviewRepository.FAKE_UID_3,
                        FakeReviewRepository.FAKE_UID_4
                    )
                )
                .setUid(FakeConnectedUser.fakeUser1.uid)
                .build()
        )

        launch()

        dislikeComment(0)
    }
}