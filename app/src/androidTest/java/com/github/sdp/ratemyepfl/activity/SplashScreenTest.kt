package com.github.sdp.ratemyepfl.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
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
class SplashScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Test
    fun userLoggedInGoesToMain() {
        init()
        FakeConnectedUser.loggedIn = true
        val intent = Intent(ApplicationProvider.getApplicationContext(), SplashScreen::class.java)
        val scenario: ActivityScenario<CourseReviewActivity> = ActivityScenario.launch(intent)
        intended(toPackage("com.github.sdp.ratemyepfl"))
        scenario.close()
        release()
    }

    @Test
    fun userPressingVisitorGoesToMain() {
        init()
        FakeConnectedUser.loggedIn = false
        val intent = Intent(ApplicationProvider.getApplicationContext(), SplashScreen::class.java)
        val scenario: ActivityScenario<CourseReviewActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.visitor_button)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        scenario.close()
        release()
    }

    @Test
    fun userPressingLogInGoesToMain() {
        init()
        FakeConnectedUser.loggedIn = false
        val intent = Intent(ApplicationProvider.getApplicationContext(), SplashScreen::class.java)
        val scenario: ActivityScenario<CourseReviewActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.login_button)).perform(click())
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, Intent())
        intending(toPackage("com.github.sdp.ratemyepfl")).respondWith(result)
        intended(toPackage("com.github.sdp.ratemyepfl"))
        scenario.close()
        release()
    }

}