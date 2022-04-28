package com.github.sdp.ratemyepfl.fragment.review

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.adapter.ReviewAdapter
import com.github.sdp.ratemyepfl.database.fakes.FakeImageStorage
import com.github.sdp.ratemyepfl.database.fakes.FakeReviewsRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeUserRepository
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import com.github.sdp.ratemyepfl.utils.TestUtils.resourceToBitmap
import com.github.sdp.ratemyepfl.utils.TestUtils.withDrawable
import com.github.sdp.ratemyepfl.utils.clickOnViewChild
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@HiltAndroidTest
class ReviewListFragmentTest {
    lateinit var scenario: ActivityScenario<ReviewActivity>

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, R.layout.activity_room_review) // can be any
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, "Fake id")
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.reviewListFragment))
    }

    @After
    fun clean() {
        scenario.close()
    }

    @Test
    fun listIsVisible() {
        onView(withId(R.id.reviewRecyclerView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun swipeRefreshes() {
        FakeReviewsRepository.reviewList = listOf(
            Review.Builder().setTitle("Absolument dé-men-tiel")
                .setId("Fake")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setDate(LocalDate.now())
                .build()
        )
        Thread.sleep(2000) // if no wait, the new list is direclty displayed
        FakeReviewsRepository.reviewList = FakeReviewsRepository.fakeList
        onView(withId(R.id.reviewRecyclerView))
            .check(matches(hasChildCount(1)))
        onView(withId(R.id.reviewSwipeRefresh)).perform(swipeDown())
        onView(withId(R.id.reviewRecyclerView))
            .check(matches(not(hasChildCount(1))))
    }

    @Test
    fun addThenRemoveLikeToReview() {
        onView(withId(R.id.reviewRecyclerView)).perform(
            actionOnItemAtPosition<ReviewAdapter.ReviewViewHolder>(
                0,
                clickOnViewChild(R.id.likeButton).also {
                    clickOnViewChild(R.id.likeButton)
                }
            )
        )
    }

    @Test
    fun addThenRemoveDislikeToReview() {
        onView(withId(R.id.reviewRecyclerView)).perform(
            actionOnItemAtPosition<ReviewAdapter.ReviewViewHolder>(
                0,
                clickOnViewChild(R.id.dislikeButton).also {
                    clickOnViewChild(R.id.dislikeButton)
                }
            )
        )
    }

    @Test
    fun usernamesOfAuthorsAreDisplayed() {
        val username1 = FakeUserRepository.userMap[FakeUserRepository.UID1]?.username
        val username2 = FakeUserRepository.userMap[FakeUserRepository.UID2]?.username
        val username3 = FakeUserRepository.userMap[FakeUserRepository.UID3]?.username
        onView(withText(username1)).check(matches(isDisplayed()))
        onView(withText(username2)).check(matches(isDisplayed()))
        onView(withText(username3)).check(matches(isDisplayed()))
    }

    @Test
    fun profilePicturesOfAuthorsAreDisplayed() {
        val username1 = FakeUserRepository.userMap[FakeUserRepository.UID1]?.username
        // load the picture
        FakeImageStorage.images.put(
            FakeUserRepository.UID1,
            ImageFile(
                "${FakeUserRepository.UID1}.jpg",
                resourceToBitmap(R.drawable.fake_profile_picture)
            )
        )
        // then refresh
        onView(withId(R.id.reviewSwipeRefresh)).perform(swipeDown())
        // then check if displayed
        onView(
            hasSibling(
                allOf(
                    withId(R.id.author_username),
                    withText(username1)
                )
            )
        ).check(matches(allOf(
            withDrawable(R.drawable.fake_profile_picture),
            isDisplayed()
        )))
    }
}