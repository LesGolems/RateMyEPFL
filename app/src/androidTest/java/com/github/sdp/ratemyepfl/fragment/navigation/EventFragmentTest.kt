package com.github.sdp.ratemyepfl.fragment.navigation

import android.Manifest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.EventAdapter
import com.github.sdp.ratemyepfl.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.database.fakes.FakeEventRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.utils.clickOnViewChild
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

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

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
    fun clickOnButtonLoggedInWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        HiltUtils.launchFragmentInHiltContainer<EventFragment> {}
        onView(withId(R.id.eventRecyclerView)).perform(
            actionOnItemAtPosition<EventAdapter.EventViewHolder>(
                0,
                clickOnViewChild(R.id.registerButton)
            )
        )
        onView(withId(R.id.eventRecyclerView))
            .check(matches(isDisplayed()))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clickOnButtonLoggedInTwiceWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        HiltUtils.launchFragmentInHiltContainer<EventFragment> {}
        onView(withId(R.id.eventRecyclerView)).perform(
            actionOnItemAtPosition<EventAdapter.EventViewHolder>(
                0,
                clickOnViewChild(R.id.registerButton).also {
                    clickOnViewChild(R.id.registerButton)
                }
            )
        )
        onView(withId(R.id.eventRecyclerView))
            .check(matches(isDisplayed()))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clickOnAddEventButtonLoggedInWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        HiltUtils.launchFragmentInHiltContainer<EventFragment> {}

        init()
        onView(withId(R.id.addEventButton)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clickOnAddEventButtonLoggedOutDoesNotWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        HiltUtils.launchFragmentInHiltContainer<EventFragment> {}

        onView(withId(R.id.addEventButton)).perform(click())
        onView(withId(R.id.addEventButton)).check(matches(isDisplayed()))
    }
}