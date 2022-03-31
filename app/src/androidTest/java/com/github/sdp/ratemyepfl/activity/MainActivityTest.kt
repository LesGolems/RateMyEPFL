package com.github.sdp.ratemyepfl.activity

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.fragment.navigation.EventFragment
import com.github.sdp.ratemyepfl.fragment.navigation.MapFragment
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import com.github.sdp.ratemyepfl.utils.CustomViewActions.navigateTo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {

    }

//    /**
//     * Template test to a BottomNavigationView
//     * The old style with supportFragmentManager does not work since we do not use the
//     * manager anymore
//     */
//    @Test
//    fun navigateHomePageAddsTheCorrectFragment() {
//        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.review))
//        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.home))
//        assertEquals(R.id.homeFragment, navController.currentDestination?.id)
//    }
//
//    @Test
//    fun pressesBackReturnsToHome() {
//        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.review))
//            .perform(navigateTo(R.id.map))
//            .perform(ViewActions.pressBack())
//        assertEquals(R.id.homeFragment, navController.currentDestination?.id)
//    }
//
//    @Test
//    fun navigateToReviewAddsTheCorrectFragment() {
//        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.review))
//        //assertEquals(R.id.reviewFragment, navController.currentDestination?.displayName)
//    }
//
//    @Test
//    fun navigateToEventPageAddsTheCorrectFragment() {
//        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.event))
//        assertEquals(R.id.eventFragment, navController.currentDestination?.id)
//    }
//
//    @Test
//    fun navigateToMapPageAddsTheCorrectFragment() {
//        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.map))
//        assertEquals(R.id.mapFragment, navController.currentDestination?.displayName)
//    }
//
//    @Test
//    fun testCourseButton() {
//        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.review))
//        init()
//        onView(withId(R.id.courseTabButton)).perform(click())
//        intended(toPackage("com.github.sdp.ratemyepfl"))
//        release()
//    }
//
//    @Test
//    fun testClassroomButton() {
//        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.review))
//        init()
//        onView(withId(R.id.reviewTabLayout)).perform(CustomViewActions.pressesTab(1))
//        onView(withId(R.id.classroomTabButton)).perform(click())
//        intended(toPackage("com.github.sdp.ratemyepfl"))
//        release()
//    }
//
//    @Test
//    fun testRestaurantButton() {
//        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(R.id.review))
//        init()
//        onView(withId(R.id.reviewTabLayout)).perform(CustomViewActions.pressesTab(2))
//        onView(withId(R.id.restaurantTabButton)).perform(click())
//        intended(toPackage("com.github.sdp.ratemyepfl"))
//        release()
//    }

    /*
    @Test
    fun firesAnIntentWhenUserPressesMapButton() {
        init()
        onView(withId(R.id.mapButton))
            .perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

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