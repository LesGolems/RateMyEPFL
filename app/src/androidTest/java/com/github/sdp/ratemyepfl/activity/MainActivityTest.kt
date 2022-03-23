package com.github.sdp.ratemyepfl.activity

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun navigateHomePageAddTheCorrectFragment() {
        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(0))
            .check(matches(isDisplayed()))
    }

    /*
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

     */

    companion object {
        fun navigateTo(itemPosition: Int) = object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(BottomNavigationView::class.java)
            }

            override fun getDescription(): String {
                return "Custom view action to press navigation view"
            }

            override fun perform(uiController: UiController?, view: android.view.View?) {
                val nav = view as BottomNavigationView
                nav.selectedItemId = itemPosition
            }

        }
    }



}