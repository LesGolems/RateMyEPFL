package com.github.sdp.ratemyepfl

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    // To be changed once the courses are implemented
    @Test
    fun firesAnIntentWhenUserPressesCourseReviewButton() {
        init()
        onView(withId(R.id.coursesReviewButton))
            .perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @Test
    fun firesAnIntentWhenUserPressesCourseButton() {
        init()
        onView(withId(R.id.coursesButton))
            .perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @Test
    fun firesAnIntentWhenUserPressesClassroomButton() {
        init()
        onView(withId(R.id.classroomReviewButton))
            .perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @Test
    fun emailDisplayedWhenUserPressesLogin() {
        onView(withId(R.id.loginButton)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.email)).check(matches(withText("user@email.com")))
    }

    @Test
    fun emailNotDisplayedWhenUserLogout() {
        onView(withId(R.id.loginButton)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.logoutButton)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.email)).check(matches(withText("")))
    }

}