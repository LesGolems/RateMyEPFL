package com.github.sdp.ratemyepfl

import android.view.KeyEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
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
    fun firesAnIntentWhenUserPressesButton() {
        init()
        val name = "John"
        onView(withId(R.id.mainName)).perform(typeText(name))
        onView(withId(R.id.mainGoButton)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @Test
    fun hasExtraWhenUserPressesButton() {
        init()
        val name = "John"
        onView(withId(R.id.mainName)).perform(typeText(name))
        onView(withId(R.id.mainGoButton)).perform(click())
        intended(hasExtra(GreetingActivity.EXTRA_USER_NAME, name))
        release()
    }

    @Test
    fun firesAnIntentWhenUserPressesEnter() {
        init()
        val name = "John"
        onView(withId(R.id.mainName)).perform(typeText(name))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @Test
    fun hasExtraWhenUserPressesEnter() {
        init()
        val name = "John"
        onView(withId(R.id.mainName)).perform(typeText(name))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))
        intended(hasExtra(GreetingActivity.EXTRA_USER_NAME, name))
        release()
    }

    @Test
    fun emailDisplayedWhenUserPressesLogin(){
        onView(withId(R.id.loginButton)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.email)).check(matches(withText("user@email.com")))
    }

    @Test
    fun emailNotDisplayedWhenUserLogout(){
        onView(withId(R.id.loginButton)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.logoutButton)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.email)).check(matches(withText("")))
    }
    /*
    @Test
    fun firesAnIntentWhenUserPressesLogin() {
        init()
        onView(withId(R.id.loginButton)).perform(click())
        intended(hasComponent("com.firebase.ui.auth.KickoffActivity"))
        release()
    }

     */
}