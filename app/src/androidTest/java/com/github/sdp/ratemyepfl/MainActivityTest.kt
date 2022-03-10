package com.github.sdp.ratemyepfl

import android.view.KeyEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.auth.FakeUserAuth
import com.github.sdp.ratemyepfl.auth.UserAuth
import com.github.sdp.ratemyepfl.dependencyinjection.DependencyInjectionModule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test

@UninstallModules(DependencyInjectionModule::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    @BindValue
    @JvmField
    val auth: UserAuth = FakeUserAuth()

    @Test
    fun firesAnIntentWhenUserPressesButton() {
        init()
        val name = "John"
        onView(withId(R.id.mainName)).perform(typeText(name))
        onView(withId(R.id.mainGoButton)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

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