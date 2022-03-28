package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.classrooms.RoomReviewActivity
import com.github.sdp.ratemyepfl.database.FakeReviewsRepository
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.utils.CustomViewActions.navigateTo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate


@HiltAndroidTest
class RoomReviewActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(RoomReviewActivity::class.java)

    @Test
    fun isIdVisibleOnActivityLaunch() {
        onView(withId(R.id.id_room_info))
            .check(matches(withText("Fake id")))
    }

    @Test
    fun isClassroomListViewVisible() {
        onView(withId(R.id.roomReviewNavigationView)).perform(navigateTo(R.id.reviewListFragment))
        onView(withId(R.id.reviewRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun isFabVisible() {
        onView(withId(R.id.roomReviewNavigationView)).perform(navigateTo(R.id.reviewListFragment))
        onView(withId(R.id.startReviewFAB))
            .check(matches(isDisplayed()))
    }

    @Test
    fun isFabShrunkWhenUserScrollsUp() {
        onView(withId(R.id.roomReviewNavigationView)).perform(navigateTo(R.id.reviewListFragment))
        onView(withId(R.id.reviewRecyclerView)).perform(
            RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                hasDescendant(withText("The last review"))
            )
        )
        onView(withId(R.id.startReviewFAB)).check(matches(isDisplayed()))
    }

    @Test
    fun fabListenForReviewIfARoomIsGiven() {
        onView(withId(R.id.roomReviewNavigationView)).perform(navigateTo(R.id.reviewListFragment))
        init()
        onView(withId(R.id.startReviewFAB)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        intended(hasExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, "Fake id"))
        release()
    }

    @Test
    fun swipeRefreshes() {
        FakeReviewsRepository.reviewList = listOf(
            Review.Builder().setTitle("Absolument dé-men-tiel")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setDate(LocalDate.now())
                .build()
        )
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            RoomReviewActivity::class.java
        )
        val scenario: ActivityScenario<RoomReviewActivity> =
            ActivityScenario.launch(intent)
        onView(withId(R.id.roomReviewNavigationView)).perform(navigateTo(R.id.reviewListFragment))
        Thread.sleep(500)
        FakeReviewsRepository.reviewList = FakeReviewsRepository.fakeList
        onView(withId(R.id.reviewRecyclerView)).check(matches(hasChildCount(1)))
        onView(withId(R.id.reviewSwipeRefresh)).perform(ViewActions.swipeDown())
        onView(withId(R.id.reviewRecyclerView)).check(matches(CoreMatchers.not(hasChildCount(1))))

        scenario.close()
    }
}