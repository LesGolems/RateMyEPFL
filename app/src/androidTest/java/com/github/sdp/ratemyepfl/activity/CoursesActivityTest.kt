package com.github.sdp.ratemyepfl.activity

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.course.CourseListActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.anything
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CoursesActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(CourseListActivity::class.java)

    @Test
    fun isCoursesListViewViewVisibleOnActivityLaunch() {
        onView(withId(R.id.coursesListView))
                .check(matches(isDisplayed()))
    }

    @Test
    fun firesAnIntentWhenUserPressesCourse() {
        init()
        onData(anything()).inAdapterView(withId(R.id.coursesListView)).atPosition(0).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }
}