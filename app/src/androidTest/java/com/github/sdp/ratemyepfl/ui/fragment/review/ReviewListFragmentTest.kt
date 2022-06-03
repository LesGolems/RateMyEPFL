package com.github.sdp.ratemyepfl.ui.fragment.review

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeCourseRepository
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeImageStorage
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeReviewRepository
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeUserRepository
import com.github.sdp.ratemyepfl.exceptions.DisconnectedUserException
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.post.Review
import com.github.sdp.ratemyepfl.model.post.ReviewRating
import com.github.sdp.ratemyepfl.model.serializer.putExtra
import com.github.sdp.ratemyepfl.model.time.DateTime
import com.github.sdp.ratemyepfl.ui.activity.ReviewActivity
import com.github.sdp.ratemyepfl.ui.adapter.post.PostAdapter
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import com.github.sdp.ratemyepfl.utils.RecyclerViewUtils.clickOnViewChild
import com.github.sdp.ratemyepfl.utils.TestUtils.checkSnackbarText
import com.github.sdp.ratemyepfl.utils.TestUtils.isExpanded
import com.github.sdp.ratemyepfl.utils.TestUtils.isHidden
import com.github.sdp.ratemyepfl.utils.TestUtils.resourceToBitmap
import com.github.sdp.ratemyepfl.utils.TestUtils.withDrawable
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ReviewListFragmentTest {
    lateinit var scenario: ActivityScenario<ReviewActivity>

    private val intent =
        Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
            .putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED_ID, "Fake id")
            .putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, FakeCourseRepository.COURSE_LIST.first())

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    fun launch() {
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.reviewTabLayout)).perform(
            CustomViewActions.TabAction.selectTabAtPosition(
                1
            )
        )
        Thread.sleep(1500)
    }

    @After
    fun clean() {
        scenario.close()
    }

    private fun refresh() {
        onView(withId(R.id.reviewTabLayout)).perform(
            CustomViewActions.TabAction.selectTabAtPosition(
                0
            )
        )
        Thread.sleep(1500)

        onView(withId(R.id.reviewTabLayout)).perform(
            CustomViewActions.TabAction.selectTabAtPosition(
                1
            )
        )
        Thread.sleep(1500)
    }

    /**
     * Like the ([position]+1)-th review
     */
    private fun likeReview(position: Int) {
        onView(withId(R.id.loadingRecyclerView)).perform(
            actionOnItemAtPosition<PostAdapter<Review>.PostViewHolder>(
                position,
                clickOnViewChild(R.id.likeButton)
            )
        )
    }

    /**
     * Dislike the ([position]+1)-th review
     */
    private fun dislikeReview(position: Int) {
        onView(withId(R.id.loadingRecyclerView)).perform(
            actionOnItemAtPosition<PostAdapter<Review>.PostViewHolder>(
                position,
                clickOnViewChild(R.id.dislikeButton)
            )
        )
    }

    // 'a' to run this test first
    @Test
    fun aPanelDisplaysCorrectUserProfileInformation() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        launch()
        val user = FakeUserRepository.userMap[FakeUserRepository.UID1]
        // load the picture
        FakeImageStorage.images[FakeUserRepository.UID1] = ImageFile(
            "${FakeUserRepository.UID1}.jpg",
            resourceToBitmap(R.raw.fake_profile_picture)
        )
        // then refresh
        onView(withId(R.id.postSwipeRefresh)).perform(swipeDown())
        // click on profile
        onView(
            allOf(
                hasSibling(
                    allOf(
                        withId(R.id.authorUsername),
                        withText(user?.username)
                    )
                ),
                hasSibling(
                    withId(R.id.deleteButton),
                )
            )
        ).perform(click())
        // checks if the information are correct
        onView(withId(R.id.authorPanelUsername)).check(matches(withText(user?.username)))
        onView(withId(R.id.authorPanelEmail)).check(matches(withText(user?.email)))
        onView(withId(R.id.author_panel_profile_image)).check(matches(withDrawable(R.raw.fake_profile_picture)))
    }

    // 'a' to run this test first
    @Test
    fun aProfilePanelDisplayedWhenAuthorIsClicked() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        launch()
        val username1 = FakeUserRepository.userMap[FakeUserRepository.UID1]?.username
        // load the picture
        FakeImageStorage.images[FakeUserRepository.UID1] = ImageFile(
            "${FakeUserRepository.UID1}.jpg",
            resourceToBitmap(R.raw.fake_profile_picture)
        )
        // then refresh
        onView(withId(R.id.postSwipeRefresh)).perform(swipeDown())
        // click on profile
        onView(
            allOf(
                hasSibling(
                    allOf(
                        withId(R.id.authorUsername),
                        withText(username1)
                    )
                ),
                hasSibling(
                    withId(R.id.deleteButton),
                )
            )
        ).perform(click())
        // check if panel shows up
        onView(withId(R.id.author_profile_panel)).check(matches(isExpanded()))
    }

    // 'a' to run this test first
    @Test
    fun aProfilePanelIsHiddenWhenClickingOutside() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        launch()
        val username1 = FakeUserRepository.userMap[FakeUserRepository.UID1]?.username
        // load the picture
        FakeImageStorage.images[FakeUserRepository.UID1] = ImageFile(
            "${FakeUserRepository.UID1}.jpg",
            resourceToBitmap(R.raw.fake_profile_picture)
        )
        // then refresh
        onView(withId(R.id.postSwipeRefresh)).perform(swipeDown())
        // click on profile
        onView(
            allOf(
                hasSibling(
                    allOf(
                        withId(R.id.authorUsername),
                        withText(username1)
                    )
                ),
                hasSibling(
                    withId(R.id.deleteButton),
                )
            )
        ).perform(click())
        // click outside
        onView(withId(R.id.author_profile_panel)).perform(click())
        // check that it is no longer exposed
        onView(withId(R.id.author_profile_panel)).check(matches(isHidden()))
    }

    // 'a' to run this test first
    @Test
    fun aProfilePicturesOfAuthorsAreDisplayed() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        launch()
        val username1 = FakeUserRepository.userMap[FakeUserRepository.UID1]?.username
        // load the picture
        FakeImageStorage.images[FakeUserRepository.UID1] = ImageFile(
            "${FakeUserRepository.UID1}.jpg",
            resourceToBitmap(R.raw.fake_profile_picture)
        )
        // then refresh
        onView(withId(R.id.postSwipeRefresh)).perform(swipeDown())
        // then check if displayed
        onView(
            allOf(
                hasSibling(
                    allOf(
                        withId(R.id.authorUsername),
                        withText(username1)
                    )
                ),
                hasSibling(
                    withId(R.id.deleteButton),
                )
            )
        ).check(
            matches(
                allOf(
                    withDrawable(R.raw.fake_profile_picture),
                    isDisplayed()
                )
            )
        )
    }

    @Test
    fun listIsVisible() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        launch()
        onView(withId(R.id.loadingRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun swipeRefreshes() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        FakeReviewRepository.reviewList = listOf(
            Review.Builder()
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setTitle("Absolument dé-men-tiel")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setTitle("Absolument dé-men-tiel")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .build()
        )
        launch()
        FakeReviewRepository.reviewList = FakeReviewRepository.fakeList
        onView(withId(R.id.loadingRecyclerView)).check(matches(hasChildCount(1)))
        onView(withId(R.id.postSwipeRefresh)).perform(swipeDown())
        onView(withId(R.id.loadingRecyclerView)).check(matches(not(hasChildCount(1))))
    }

    @Test
    fun likeThenLikeReview() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        FakeReviewRepository.reviewList = listOf(
            Review.Builder()
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setTitle("Absolument dé-men-tiel")
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
        )
        launch()

        likeReview(0)
        onView(withId(R.id.likeCount)).check(matches(withText("1")))
        likeReview(0)
        onView(withId(R.id.likeCount)).check(matches(withText("0")))
    }

    @Test
    fun dislikeThenDislikeReview() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        FakeReviewRepository.reviewList = listOf(
            Review.Builder()
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setTitle("Absolument dé-men-tiel")
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
        )
        launch()

        dislikeReview(0)
        onView(withId(R.id.dislikeCount)).check(matches(withText("1")))
        dislikeReview(0)
        onView(withId(R.id.dislikeCount)).check(matches(withText("0")))
    }

    @Test
    fun likeThenDislikeReview() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        FakeReviewRepository.reviewList = listOf(
            Review.Builder()
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setTitle("Absolument dé-men-tiel")
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
        )
        launch()

        likeReview(0)
        onView(withId(R.id.likeCount)).check(matches(withText("1")))
        dislikeReview(0)
        onView(withId(R.id.dislikeCount)).check(matches(withText("1")))
    }

    @Test
    fun dislikeThenLikeReview() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_2
        FakeReviewRepository.reviewList = listOf(
            Review.Builder()
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setTitle("Absolument dé-men-tiel")
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
        )
        launch()

        dislikeReview(0)
        onView(withId(R.id.dislikeCount)).check(matches(withText("1")))
        likeReview(0)
        onView(withId(R.id.likeCount)).check(matches(withText("1")))
    }

    @Test
    fun likeWhenNotConnected() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        launch()
        likeReview(0)
        checkSnackbarText(DisconnectedUserException.DEFAULT_ERROR_MSG)
    }

    @Test
    fun dislikeWhenNotConnected() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        launch()
        dislikeReview(0)
        checkSnackbarText(DisconnectedUserException.DEFAULT_ERROR_MSG)
    }

    @Test
    fun deleteButtonRemovesReview() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        FakeReviewRepository.reviewList = listOf(
            Review.Builder()
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setTitle("Absolument dé-men-tiel")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .setUid(FakeConnectedUser.fakeUser2.uid)
                .build()
        )

        launch()

        onView(withId(R.id.loadingRecyclerView)).perform(
            actionOnItemAtPosition<PostAdapter<Review>.PostViewHolder>(
                0,
                clickOnViewChild(R.id.deleteButton)
            )
        )

        refresh()

        assertEquals(listOf<Review>(), FakeReviewRepository.reviewList)
    }

    @Test
    fun usernamesOfAuthorsAreDisplayed() {
        val username1 = FakeUserRepository.userMap[FakeUserRepository.UID1]?.username
        val username2 = FakeUserRepository.userMap[FakeUserRepository.UID2]?.username
        val username3 = FakeUserRepository.userMap[FakeUserRepository.UID3]?.username

        launch()

        onView(withText(username1)).check(matches(isDisplayed()))
        onView(withText(username2)).check(matches(isDisplayed()))
        onView(withText(username3)).check(matches(isDisplayed()))
    }

    @Test
    fun likeItsOwnReviewThrowsException() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        FakeReviewRepository.reviewList = listOf(
            Review.Builder()
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setTitle("Absolument dé-men-tiel")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .setUid(FakeConnectedUser.fakeUser1.uid)
                .build()
        )

        launch()

        likeReview(0)
        checkSnackbarText("You can't like your own review")
    }

    @Test
    fun dislikeItsOwnReviewThrowsException() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        FakeReviewRepository.reviewList = listOf(
            Review.Builder()
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setTitle("Absolument dé-men-tiel")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setDate(DateTime.now())
                .setUid(FakeConnectedUser.fakeUser1.uid)
                .build()
        )

        launch()

        dislikeReview(0)
        checkSnackbarText("You can't dislike your own review")
    }
}