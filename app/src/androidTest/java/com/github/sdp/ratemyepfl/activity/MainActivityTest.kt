package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.fragment.navigation.EventFragment
import com.github.sdp.ratemyepfl.fragment.navigation.HomeFragment
import com.github.sdp.ratemyepfl.fragment.navigation.MapFragment
import com.github.sdp.ratemyepfl.fragment.navigation.ReviewFragment
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import com.github.sdp.ratemyepfl.utils.CustomViewActions.navigateTo
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule (order = 2)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun initiallyStartAtHomePage() {
        testRule.scenario.recreate()
            .onActivity { activity ->
                val fragment: Fragment? =
                    activity.supportFragmentManager.findFragmentById(R.id.mainActivityFragmentContainer)
                assertEquals(true, fragment is HomeFragment)
            }
    }

    @Test
    fun navigateHomePageAddsTheCorrectFragment() {
        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.homeNavItem))
        testRule.scenario.onActivity { activity ->
            val fragment: Fragment? =
                activity.supportFragmentManager.findFragmentById(R.id.mainActivityFragmentContainer)
            assertEquals(true, fragment is HomeFragment)
        }
    }

    @Test
    fun navigateToReviewAddsTheCorrectFragment() {
        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.reviewNavItem))
        testRule.scenario.onActivity { activity ->
            val fragment: Fragment? =
                activity.supportFragmentManager.findFragmentById(R.id.mainActivityFragmentContainer)
            assertEquals(true, fragment is ReviewFragment)
        }
    }

    @Test
    fun navigateToEventPageAddsTheCorrectFragment() {
        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.eventNavItem))
        testRule.scenario.onActivity { activity ->
            activity.findViewById<BottomNavigationView>(R.id.activityMainBottomNavigationView).selectedItemId = 2
            val fragment: Fragment? =
                activity.supportFragmentManager.findFragmentById(R.id.mainActivityFragmentContainer)
            assertEquals(true, fragment is EventFragment)
        }
    }

    @Test
    fun navigateToMapPageAddsTheCorrectFragment() {
        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.mapNavItem))
        testRule.scenario.onActivity { activity ->
            val fragment: Fragment? =
                activity.supportFragmentManager.findFragmentById(R.id.mainActivityFragmentContainer)
            assertEquals(true, fragment is MapFragment)
        }
    }

    @Test
    fun testCourseButton() {
        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.reviewNavItem))
        init()
        onView(withId(R.id.courseTabButton)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @Test
    fun testClassroomButton() {
        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.reviewNavItem))
        init()
        onView(withId(R.id.reviewTabLayout)).perform(CustomViewActions.pressesTab(1))
        onView(withId(R.id.classroomTabButton)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
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



}