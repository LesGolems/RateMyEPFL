package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.FakeConnectedUser
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
        FakeConnectedUser.loggedIn = true
        val intent = Intent(ApplicationProvider.getApplicationContext(), SplashScreen::class.java)
        val scenario: ActivityScenario<SplashScreen> = ActivityScenario.launch(intent)
        intended(IntentMatchers.hasComponent("com.github.sdp.ratemyepfl.activity.MainActivity"))
        scenario.close()
        release()
    }

    @Test
    fun userPressingVisitorGoesToMain() {
        init()
        FakeConnectedUser.loggedIn = false
        val intent = Intent(ApplicationProvider.getApplicationContext(), SplashScreen::class.java)
        val scenario: ActivityScenario<SplashScreen> = ActivityScenario.launch(intent)
        onView(withId(R.id.visitorButton)).perform(click())
        intended(IntentMatchers.hasComponent("com.github.sdp.ratemyepfl.activity.MainActivity"))
        scenario.close()
        release()
    }

    @Test
    fun userPressingLogInGoesToMain() {
        init()
        FakeConnectedUser.loggedIn = false
        val intent = Intent(ApplicationProvider.getApplicationContext(), SplashScreen::class.java)
        val scenario: ActivityScenario<SplashScreen> = ActivityScenario.launch(intent)
        FakeConnectedUser.loggedIn = true
        onView(withId(R.id.loginButton)).perform(click())
        intended(IntentMatchers.hasComponent("com.firebase.ui.auth.KickoffActivity"))
        val device = UiDevice.getInstance(getInstrumentation())
        device.pressBack()
        scenario.close()
        release()
    }

}