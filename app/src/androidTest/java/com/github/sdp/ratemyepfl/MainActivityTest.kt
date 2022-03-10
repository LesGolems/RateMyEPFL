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
}