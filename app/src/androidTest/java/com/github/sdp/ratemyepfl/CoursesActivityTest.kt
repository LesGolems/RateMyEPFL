package com.github.sdp.ratemyepfl

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test

class CoursesActivityTest {
    @Test
    fun isCoursesListViewViewVisibleOnActivityLaunch() {
        onView(withId(R.id.coursesListView))
                .check(matches(isDisplayed()))
    }

    @Test
    fun firesAnIntentWhenUserPressesCourse() {
        init()
        onView(withId(R.id.coursesListView))
                .perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }
}