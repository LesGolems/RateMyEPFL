package com.github.sdp.ratemyepfl.ui.fragment.main

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeCommentRepository
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeReviewRepository
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeUserRepository
import com.github.sdp.ratemyepfl.backend.database.post.CommentRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.model.post.Comment
import com.github.sdp.ratemyepfl.model.post.Review
import com.github.sdp.ratemyepfl.model.time.DateTime
import com.github.sdp.ratemyepfl.ui.adapter.post.PostAdapter
import com.github.sdp.ratemyepfl.utils.RecyclerViewUtils
import com.github.sdp.ratemyepfl.utils.TestUtils.checkSnackbarText
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.not
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class CommentListFragmentTest {

    @get:Rule
    val hiltAndroidTestRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: CommentRepository

    @Before
    fun setUp() {
        hiltAndroidTestRule.inject()
    }

    fun launch() {
        val b = Bundle()
        b.putString(CommentListFragment.EXTRA_SUBJECT_COMMENTED_ID, "fake id")
        HiltUtils.launchFragmentInHiltContainer<CommentListFragment>(b)
    }

    /**
     * Like the ([position]+1)-th comment
     */
    private fun likeComment(position: Int) {
        onView(withId(R.id.loadingRecyclerView)).perform(
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
        onView(withId(R.id.loadingRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PostAdapter<Review>.PostViewHolder>(
                position,
                RecyclerViewUtils.clickOnViewChild(R.id.dislikeButton)
            )
        )
    }

    private fun refresh() {
        onView(withId(R.id.postSwipeRefresh)).perform(swipeDown())
    }

    @Test
    fun addCommentWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        launch()

        onView(withId(R.id.author_profile_panel)).perform(swipeUp())
        onView(withId(R.id.addComment)).perform(typeText("my comment"))
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.doneButton)).check(matches(not(isDisplayed())))
    }

    @Test
    fun addEmptyCommentDoesNotWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        launch()

        onView(withId(R.id.commentPanel)).perform(swipeUp())
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.doneButton)).check(matches(isDisplayed()))
    }

    @Test
    fun addAnonymousCommentWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        launch()

        onView(withId(R.id.commentPanel)).perform(swipeUp())
        onView(withId(R.id.addComment)).perform(typeText("my comment"))
        onView(withId(R.id.addCommentAnonymousSwitch)).perform(click())
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.doneButton)).check(matches(not(isDisplayed())))
    }

    @Test
    fun addCommentNotLoggedInDoesNotWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        launch()

        onView(withId(R.id.commentPanel)).perform(swipeUp())
        onView(withId(R.id.addComment)).perform(typeText("my comment"))
        onView(withId(R.id.doneButton)).check(matches(isDisplayed()))
    }

    @Test
    fun likeThenLikeReview() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        (repository as FakeCommentRepository).elements = listOf(
            Comment.Builder()
                .setSubjectID("id")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .setLikers(
                    listOf()
                )
                .setDislikers(
                    listOf()
                )
                .setUid(FakeUserRepository.UID1)
                .build()
        ).toSet()
        launch()

        likeComment(0)
        onView(withId(R.id.likeCount)).check(matches(withText("1")))
        likeComment(0)
        onView(withId(R.id.likeCount)).check(matches(withText("0")))
    }

    @Test
    fun dislikeThenDislikeReview() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        (repository as FakeCommentRepository).elements = listOf(
            Comment.Builder()
                .setSubjectID("id")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .setLikers(
                    listOf()
                )
                .setDislikers(
                    listOf()
                )
                .setUid(FakeUserRepository.UID1)
                .build()
        ).toSet()
        launch()

        dislikeComment(0)
        onView(withId(R.id.dislikeCount)).check(matches(withText("1")))
        dislikeComment(0)
        onView(withId(R.id.dislikeCount)).check(matches(withText("0")))
    }

    @Test
    fun likeThenDislikeReview() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        (repository as FakeCommentRepository).elements = listOf(
            Comment.Builder()
                .setSubjectID("id")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .setLikers(
                    listOf()
                )
                .setDislikers(
                    listOf()
                )
                .setUid(FakeUserRepository.UID1)
                .build()
        ).toSet()
        launch()

        likeComment(0)
        onView(withId(R.id.likeCount)).check(matches(withText("1")))
        dislikeComment(0)
        onView(withId(R.id.dislikeCount)).check(matches(withText("1")))
    }

    @Test
    fun dislikeThenLikeReview() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        (repository as FakeCommentRepository).elements = listOf(
            Comment.Builder()
                .setSubjectID("id")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .setLikers(
                    listOf()
                )
                .setDislikers(
                    listOf()
                )
                .setUid(FakeUserRepository.UID1)
                .build()
        ).toSet()
        launch()

        dislikeComment(0)
        onView(withId(R.id.dislikeCount)).check(matches(withText("1")))
        likeComment(0)
        onView(withId(R.id.likeCount)).check(matches(withText("1")))
    }

    @Test
    fun likeWhenNotConnected() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        launch()
        likeComment(0)
        checkSnackbarText(DisconnectedUserException.DEFAULT_ERROR_MSG)
    }

    @Test
    fun dislikeWhenNotConnected() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        launch()
        dislikeComment(0)
        checkSnackbarText(DisconnectedUserException.DEFAULT_ERROR_MSG)
    }

    @Test
    fun deleteButtonRemovesComment() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        (repository as FakeCommentRepository).elements = listOf(
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
        ).toSet()

        launch()

        onView(withId(R.id.loadingRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PostAdapter<Review>.PostViewHolder>(
                0,
                RecyclerViewUtils.clickOnViewChild(R.id.deleteButton)
            )
        )

        refresh()

        Assert.assertEquals(true, (repository as FakeCommentRepository).elements.isEmpty())
    }

    @Test
    fun likeItsOwnReviewThrowsException() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        (repository as FakeCommentRepository).elements = listOf(
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
        ).toSet()

        launch()

        likeComment(0)

        checkSnackbarText("You can't like your own review")
    }

    @Test
    fun dislikeItsOwnReviewThrowsException() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        (repository as FakeCommentRepository).elements = listOf(
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
        ).toSet()

        launch()

        dislikeComment(0)

        checkSnackbarText("You can't dislike your own review")
    }
}