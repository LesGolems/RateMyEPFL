package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.FakeReviewsRepository
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@HiltAndroidTest
class CourseReviewListActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(CourseReviewListActivity::class.java)

    @Test
    fun isCoursesListViewVisibleOnActivityLaunch() {
        onView(withId(R.id.reviewRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun isFabVisibleOnActivityLaunch() {
        onView(withId(R.id.startReviewFAB)).check(matches(isDisplayed()))
    }

    @Test
    fun fabListenForReviewIfACourseIsGiven() {
        val course = Course("Software development project", "IC", "George Candea", 4, "CS-306")
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            CourseReviewListActivity::class.java
        )
        intent.putExtra(
            CourseReviewListActivity.EXTRA_COURSE_JSON,
            Json.encodeToString(course)
        )

        val scenario: ActivityScenario<CourseReviewListActivity> =
            ActivityScenario.launch(intent)

        init()
        onView(withId(R.id.startReviewFAB)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        intended(hasExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, course.id))
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
            CourseReviewListActivity::class.java
        )
        val scenario: ActivityScenario<CourseReviewListActivity> =
            ActivityScenario.launch(intent)
        FakeReviewsRepository.reviewList = FakeReviewsRepository.fakeList

        onView(withId(R.id.reviewRecyclerView)).check(matches(hasChildCount(1)))
        onView(withId(R.id.swiperefresh)).perform(swipeDown())
        onView(withId(R.id.reviewRecyclerView)).check(matches(not(hasChildCount(1))))

        scenario.close()
    }

}