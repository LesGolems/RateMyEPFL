package com.github.sdp.ratemyepfl

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
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
    fun test() {
        init()

        val username = "Julien"
        onView(withId(R.id.mainName)).perform(typeText(username))
        onView(withId(R.id.mainGoButton)).perform(click())

        /* Check an intent is fired */
        intended(toPackage("com.github.sdp.ratemyepfl"));

        /* Check the intent contains the username */
        intended(hasExtra(GreetingActivity.EXTRA_USER_NAME, username));

        release();
    }

}