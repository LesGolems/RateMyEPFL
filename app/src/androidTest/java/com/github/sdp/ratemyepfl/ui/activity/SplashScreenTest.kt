package com.github.sdp.ratemyepfl.ui.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.ui.activity.SplashScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

/**
 * Global notes on test: for some reason, the intent instrumented by the tests
 * does not have the test package as target, and is counted twice. It only
 * appears with the fragment testing library
 */
@HiltAndroidTest
class SplashScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Test
    fun userLoggedInGoesToMain() {
        init()
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), SplashScreen::class.java)
        val scenario: ActivityScenario<SplashScreen> = ActivityScenario.launch(intent)
        intended(IntentMatchers.hasComponent("com.github.sdp.ratemyepfl.ui.activity.MainActivity"))
        scenario.close()
        release()
    }

    @Test
    fun userPressingVisitorGoesToMain() {
        init()
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        val intent = Intent(ApplicationProvider.getApplicationContext(), SplashScreen::class.java)
        val scenario: ActivityScenario<SplashScreen> = ActivityScenario.launch(intent)
        onView(withId(R.id.visitorButton)).perform(click())
        intended(IntentMatchers.hasComponent("com.github.sdp.ratemyepfl.activity.MainActivity"))
        scenario.close()
        release()
    }

    @Test
    fun userPressingLogInGoesToGoogle() {
        init()
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        val intent = Intent(ApplicationProvider.getApplicationContext(), SplashScreen::class.java)
        val scenario: ActivityScenario<SplashScreen> = ActivityScenario.launch(intent)
        onView(withId(R.id.loginButton)).perform(click())
        intended(IntentMatchers.hasComponent("com.firebase.ui.auth.KickoffActivity"))
        scenario.close()
        release()
    }

}