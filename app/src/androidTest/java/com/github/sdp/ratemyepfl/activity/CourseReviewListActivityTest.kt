package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.course.CourseReviewListActivity
import com.github.sdp.ratemyepfl.model.items.Course
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.Test

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

    @HiltAndroidTest
    class NoRule {
        @get:Rule(order = 0)
        val hiltRule = HiltAndroidRule(this)

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
    }

}