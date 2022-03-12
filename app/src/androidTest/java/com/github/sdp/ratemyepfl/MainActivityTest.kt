package com.github.sdp.ratemyepfl

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.auth.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    // To be changed once the courses are implemented
    @Test
    fun firesAnIntentWhenUserPressesCourseReviewButton() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

        val scenario: ActivityScenario<CourseReviewActivity> = ActivityScenario.launch(intent)
        init()
        onView(withId(R.id.coursesReviewButton))
            .perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
        scenario.close()
    }

    @Test
    fun firesAnIntentWhenUserPressesClassroomButton() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

        val scenario: ActivityScenario<CourseReviewActivity> = ActivityScenario.launch(intent)
        init()
        onView(withId(R.id.classroomReviewButton))
            .perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
        scenario.close()
    }

    @Test
    fun firesAnIntentWhenUserPressesCoursesButton() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

        val scenario: ActivityScenario<CourseReviewActivity> = ActivityScenario.launch(intent)
        init()
        onView(withId(R.id.coursesButton))
            .perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
        scenario.close()
    }

    @Test
    fun firesAnIntentWhenUserPressesLogin() {
        FakeConnectedUser.loggedIn = false
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

        val scenario: ActivityScenario<CourseReviewActivity> = ActivityScenario.launch(intent)
        init()
        onView(withId(R.id.loginButton))
            .perform(click())
        intended(IntentMatchers.hasComponent("com.firebase.ui.auth.KickoffActivity"))
        release()
        scenario.close()
    }

    @Test
    fun emailDisplayedWhenUserisLoggedIn() {
        FakeConnectedUser.loggedIn = true
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

        val scenario: ActivityScenario<CourseReviewActivity> = ActivityScenario.launch(intent)
        init()
        onView(withId(R.id.logoutButton)).perform(click())
        onView(withId(R.id.email)).check(matches(withText("user@email.com")))
        release()
        scenario.close()
    }

    @Test
    fun emailDisplayedWhenUserisLoggedOut() {
        FakeConnectedUser.loggedIn = false
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

        val scenario: ActivityScenario<CourseReviewActivity> = ActivityScenario.launch(intent)
        init()
        onView(withId(R.id.email)).check(matches(withText("")))
        release()
        scenario.close()
    }

}