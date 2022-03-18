package com.github.sdp.ratemyepfl.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.course.CourseListActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CourseListActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(CourseListActivity::class.java)

    @Test
    fun isRecyclerViewVisibleOnActivityLaunch() {
        onView(withId(R.id.courses_recycler_view))
            .check(matches(isDisplayed()))
    }

    /**
    @Test
    fun firesAnIntentWhenUserClicksOnCourse() {
        init()
        onView(withId(R.id.courses_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, click()
                )
            )
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }*/
}