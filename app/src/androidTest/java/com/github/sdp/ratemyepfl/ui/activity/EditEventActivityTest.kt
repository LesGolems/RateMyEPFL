package com.github.sdp.ratemyepfl.ui.activity

import android.Manifest
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.FakeConnectedUser
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class EditEventActivityTest {
    lateinit var scenario: ActivityScenario<EditEventActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

    @After
    fun clean() {
        scenario.close()
    }

    @Test
    fun clickOnDoneBeforeFillingDoesNotWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), EditEventActivity::class.java)
        intent.putExtra(EditEventActivity.EXTRA_IS_NEW_EVENT, true)
        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.doneButton)).perform(scrollTo())
        onView(withId(R.id.doneButton)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.doneButton)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnDoneHavingFilledOnlyNameDoesNotWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), EditEventActivity::class.java)
        intent.putExtra(EditEventActivity.EXTRA_IS_NEW_EVENT, true)
        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.editEventName)).perform(typeText("Truc de fou"), closeSoftKeyboard())
        onView(withId(R.id.doneButton)).perform(scrollTo())
        onView(withId(R.id.doneButton)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.doneButton)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnDoneLoggedOutDoesNotWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), EditEventActivity::class.java)
        intent.putExtra(EditEventActivity.EXTRA_IS_NEW_EVENT, true)
        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.doneButton)).perform(scrollTo())
        onView(withId(R.id.doneButton)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.doneButton)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnCancelButtonWorks() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), EditEventActivity::class.java)
        intent.putExtra(EditEventActivity.EXTRA_IS_NEW_EVENT, true)
        scenario = ActivityScenario.launch(intent)

        init()
        onView(withId(R.id.cancelButton)).perform(scrollTo())
        onView(withId(R.id.cancelButton)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @Test
    fun swipeOnTheFillersWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), EditEventActivity::class.java)
        intent.putExtra(EditEventActivity.EXTRA_IS_NEW_EVENT, true)
        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.limitPicker)).perform(swipeDown())
        onView(withId(R.id.datePicker)).perform(swipeDown())
        onView(withId(R.id.timePicker)).perform(scrollTo())
        onView(withId(R.id.timePicker)).perform(swipeDown())
        onView(withId(R.id.timePicker)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnMapWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), EditEventActivity::class.java)
        intent.putExtra(EditEventActivity.EXTRA_IS_NEW_EVENT, true)
        scenario = ActivityScenario.launch(intent)

        onView(withId(R.id.mapContainer)).perform(scrollTo())
        onView(withId(R.id.mapContainer)).perform(swipeUp())
        onView(withId(R.id.mapContainer)).perform(click())
        onView(withId(R.id.mapContainer)).check(matches(isDisplayed()))
    }

    @Test
    fun submittingWhenFilledWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), EditEventActivity::class.java)
        intent.putExtra(EditEventActivity.EXTRA_IS_NEW_EVENT, false)
        intent.putExtra(EditEventActivity.EXTRA_EVENT_ID, "fake")
        intent.putExtra(EditEventActivity.EXTRA_EVENT_TITLE, "fake")
        intent.putExtra(EditEventActivity.EXTRA_EVENT_LIM_PART, 50)
        intent.putExtra(EditEventActivity.EXTRA_EVENT_TIME, intArrayOf(11, 30))
        intent.putExtra(EditEventActivity.EXTRA_EVENT_DATE, intArrayOf(2022, 5, 12))
        intent.putExtra(EditEventActivity.EXTRA_EVENT_LOCATION, doubleArrayOf(0.0, 0.0))
        scenario = ActivityScenario.launch(intent)

        init()
        onView(withId(R.id.doneButton)).perform(scrollTo())
        onView(withId(R.id.doneButton)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @Test
    fun submittingWhenFilledWorkForNewEvent() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), EditEventActivity::class.java)
        intent.putExtra(EditEventActivity.EXTRA_IS_NEW_EVENT, true)
        intent.putExtra(EditEventActivity.EXTRA_EVENT_TITLE, "fake")
        intent.putExtra(EditEventActivity.EXTRA_EVENT_LIM_PART, 50)
        intent.putExtra(EditEventActivity.EXTRA_EVENT_TIME, intArrayOf(11, 30))
        intent.putExtra(EditEventActivity.EXTRA_EVENT_DATE, intArrayOf(2022, 5, 12))
        intent.putExtra(EditEventActivity.EXTRA_EVENT_LOCATION, doubleArrayOf(0.0, 0.0))
        scenario = ActivityScenario.launch(intent)

        init()
        onView(withId(R.id.doneButton)).perform(scrollTo())
        onView(withId(R.id.doneButton)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }
}