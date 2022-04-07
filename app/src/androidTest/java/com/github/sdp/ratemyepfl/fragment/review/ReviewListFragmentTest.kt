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
import com.github.sdp.ratemyepfl.database.FakeReviewsRepository
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import com.github.sdp.ratemyepfl.utils.clickOnViewChild
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
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
        Thread.sleep(500) // if no wait, the new list is direclty displayed
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
}