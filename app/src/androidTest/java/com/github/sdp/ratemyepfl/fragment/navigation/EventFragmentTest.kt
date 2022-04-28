package com.github.sdp.ratemyepfl.fragment.navigation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.database.fakes.FakeEventRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class EventFragmentTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Test
    fun startReviewOnClick() {
        HiltUtils.launchFragmentInHiltContainer<EventFragment> {}
        init()
        onView(withText(FakeEventRepository.EVENT_LIST[0].toString()))
            .perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clickOnButtonLoggedWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        HiltUtils.launchFragmentInHiltContainer<EventFragment> {}
        onView(withId(R.id.registerButton))
            .perform(click())
        onView(withId(R.id.registerButton))
            .check(matches(isDisplayed()))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clickOnButtonLoggedTwiceWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        HiltUtils.launchFragmentInHiltContainer<EventFragment> {}
        onView(withId(R.id.registerButton))
            .perform(click())
        onView(withId(R.id.registerButton))
            .perform(click())
        onView(withId(R.id.registerButton))
            .check(matches(isDisplayed()))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clickOnButtonNotLoggedWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        HiltUtils.launchFragmentInHiltContainer<EventFragment> {}
        onView(withId(R.id.registerButton))
            .perform(click())
        onView(withId(R.id.registerButton))
            .check(matches(isDisplayed()))
    }
}