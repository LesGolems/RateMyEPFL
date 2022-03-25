package com.github.sdp.ratemyepfl.activity

import android.app.Activity
import android.app.Instrumentation
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
import com.github.sdp.ratemyepfl.activity.classrooms.RoomReviewsListActivity
import com.github.sdp.ratemyepfl.activity.course.CourseReviewListActivity
import com.github.sdp.ratemyepfl.database.FakeReviewsRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate


@HiltAndroidTest
class RoomReviewsListActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(CourseReviewListActivity::class.java)

    @Test
    fun isCoursesListViewVisibleOnActivityLaunch() {
        onView(withId(R.id.reviewRecyclerView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun isFabVisibleOnActivityLaunch() {
        onView(withId(R.id.startReviewFAB))
            .check(matches(isDisplayed()))
    }

    @Test
    fun isFabShrunkWhenUserScrollsUp() {
        onView(withId(R.id.reviewRecyclerView)).perform(
            RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                hasDescendant(withText("The last review"))
            )
        )
        onView(withId(R.id.startReviewFAB)).check(matches(isDisplayed()))
    }

    @Test
    fun fabListenForReviewIfARoomIsGiven() {
        val room = Classroom("CM3")
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            RoomReviewsListActivity::class.java
        )
        intent.putExtra(
            RoomReviewsListActivity.EXTRA_CLASSROOMS_JSON,
            Json.encodeToString(room)
        )

        val scenario: ActivityScenario<RoomReviewsListActivity> =
            ActivityScenario.launch(intent)

        init()
        onView(withId(R.id.startReviewFAB)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        intended(hasExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, room.id))
        release()
        scenario.close()
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
            RoomReviewsListActivity::class.java
        )
        val scenario: ActivityScenario<RoomReviewsListActivity> =
            ActivityScenario.launch(intent)
        FakeReviewsRepository.reviewList = FakeReviewsRepository.fakeList

        onView(withId(R.id.reviewRecyclerView)).check(matches(hasChildCount(1)))
        onView(withId(R.id.swiperefresh)).perform(ViewActions.swipeDown())
        onView(withId(R.id.reviewRecyclerView)).check(matches(CoreMatchers.not(hasChildCount(1))))

        scenario.close()
    }


}