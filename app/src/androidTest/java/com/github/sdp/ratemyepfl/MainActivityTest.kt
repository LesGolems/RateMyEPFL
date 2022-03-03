package com.github.sdp.ratemyepfl

import android.view.KeyEvent
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.*

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
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
}