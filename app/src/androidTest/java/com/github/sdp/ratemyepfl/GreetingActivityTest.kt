package com.github.sdp.ratemyepfl

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.assertion.ViewAssertions.matches

import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.Espresso.onView


import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingActivityTest {

    @Test
    fun test() {
        val username = "Julien"
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), GreetingActivity::class.java)
        intent.putExtra(GreetingActivity.EXTRA_USER_NAME, username)

        val scenario: ActivityScenario<GreetingActivity> = ActivityScenario.launch(intent)
        onView(withId(R.id.greetingMessage)).check(
            matches(
                withText(
                    "Hello $username!"
                )
            )
        )
        scenario.close()
    }

    @Test
    fun displaysUsernameOnLaunch() {
        val name = "name"
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            GreetingActivity::class.java
        )
            .putExtra(GreetingActivity.EXTRA_USER_NAME, name)

        val scenario: ActivityScenario<GreetingActivity> = ActivityScenario.launch(intent)
        scenario.use {
            onView(withId(R.id.greetingMessage)).check(
                matches(
                    withText("Hello $name!")
                )
            )
        }
    }
}