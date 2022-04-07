package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeFragmentTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Test
    fun displayTheCorrectTextWhenUserIsNotLoggedIn() {
        FakeConnectedUser.loggedIn = false
        FakeConnectedUser.email = null
        val welcomeMsg = ApplicationProvider.getApplicationContext<Context>().resources.getString(
            R.string.home_page_hello_user_text,
            HomeFragment.VISITOR_NAME
        )
        HiltUtils.launchFragmentInHiltContainer<HomeFragment> {
        }

        onView(withId(R.id.homePageHelloUserText)).check(matches(withText(welcomeMsg)))
        onView(withId(R.id.homePageConnectionButton)).check(matches(withText(HomeFragment.LOGIN)))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun displayTheCorrectTextWhenUserIsLoggedIn() {
        FakeConnectedUser.loggedIn = true
        val email = "john@example.com"
        FakeConnectedUser.email = email
        val welcomeMsg = ApplicationProvider.getApplicationContext<Context>().resources.getString(
            R.string.home_page_hello_user_text,
            email
        )
        HiltUtils.launchFragmentInHiltContainer<HomeFragment> {}

        onView(withId(R.id.homePageHelloUserText)).check(matches(withText(welcomeMsg)))
        onView(withId(R.id.homePageConnectionButton)).check(matches(withText(HomeFragment.LOGOUT)))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun updateToLoginWhenUserPressesLogout() {
        FakeConnectedUser.loggedIn = true
        val email = "john@example.com"
        FakeConnectedUser.email = email
        HiltUtils.launchFragmentInHiltContainer<HomeFragment> {}
        FakeConnectedUser.loggedIn = false
        onView(withId(R.id.homePageConnectionButton)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.homePageConnectionButton)).check(matches(withText(HomeFragment.LOGIN)))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loginGoesToSplashScreen() {
        FakeConnectedUser.loggedIn = false
        init()
        HiltUtils.launchFragmentInHiltContainer<HomeFragment> {}
        onView(withId(R.id.homePageConnectionButton)).perform(click())
        intended(IntentMatchers.hasComponent("com.github.sdp.ratemyepfl.activity.SplashScreen"))
        release()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun updateToVisitorWhenUserPressesLogout() {
        FakeConnectedUser.loggedIn = true
        val email = "john@example.com"
        FakeConnectedUser.email = email
        val welcomeMsg = ApplicationProvider.getApplicationContext<Context>().resources.getString(
            R.string.home_page_hello_user_text,
            HomeFragment.VISITOR_NAME
        )
        HiltUtils.launchFragmentInHiltContainer<HomeFragment> {}
        FakeConnectedUser.loggedIn = false
        onView(withId(R.id.homePageConnectionButton)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.homePageHelloUserText)).check(
            matches(withText(welcomeMsg))
        )
    }


}