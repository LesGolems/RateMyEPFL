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
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
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
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val email = "john@example.com"
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
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        HiltUtils.launchFragmentInHiltContainer<HomeFragment> {}
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        onView(withId(R.id.homePageConnectionButton)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.homePageConnectionButton)).check(matches(withText(HomeFragment.LOGIN)))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loginGoesToSplashScreen() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        init()
        HiltUtils.launchFragmentInHiltContainer<HomeFragment> {}
        onView(withId(R.id.homePageConnectionButton)).perform(click())
        intended(IntentMatchers.hasComponent("com.github.sdp.ratemyepfl.activity.SplashScreen"))
        release()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun updateToVisitorWhenUserPressesLogout() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val welcomeMsg = ApplicationProvider.getApplicationContext<Context>().resources.getString(
            R.string.home_page_hello_user_text,
            HomeFragment.VISITOR_NAME
        )
        HiltUtils.launchFragmentInHiltContainer<HomeFragment> {}
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        onView(withId(R.id.homePageConnectionButton)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.homePageHelloUserText)).check(
            matches(withText(welcomeMsg))
        )
    }


}