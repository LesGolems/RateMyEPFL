package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.FakeConnectedUser
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
    fun firesAnIntentWhenUserPressesMapButton() {
        init()
        onView(withId(R.id.mapButton))
            .perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @Test
    fun emailDisplayedWhenUserLoggedIn() {
        FakeConnectedUser.loggedIn = true
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

        val scenario: ActivityScenario<MainActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.userText)).check(matches(withText("user@email.com")))
        scenario.close()
    }

    @Test
    fun logoutGoesToSplashScreen() {
        init()
        FakeConnectedUser.loggedIn = true
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        val scenario: ActivityScenario<MainActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.logoutButton)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        scenario.close()
        release()
    }

    @Test
    fun visitorDisplayedWhenLoggedOut() {
        FakeConnectedUser.loggedIn = false
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        val scenario: ActivityScenario<MainActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.userText)).check(matches(withText("Visitor")))
        scenario.close()
    }

}