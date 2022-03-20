package com.github.sdp.ratemyepfl.activity

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.course.CourseListActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CourseListActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(CourseListActivity::class.java)

    @Test
    fun isRecyclerViewVisibleOnActivityLaunch() {
        onView(withId(R.id.reviewableRecyclerView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun firesAnIntentWhenUserClicksOnReviewButton() {
        Intents.init()
        onView(withId(R.id.reviewableRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, clickOnViewChild(R.id.reviewableButton)
                )
            )
        Intents.intended(IntentMatchers.toPackage("com.github.sdp.ratemyepfl"))
        Intents.release()
    }

    /*
    @Test
    fun hasExtraWhenUserClicksOnReviewButton() {
        Intents.init()
        onView(withId(R.id.reviewableRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, clickOnViewChild(R.id.reviewableButton)
                )
            )

        Intents.intended(IntentMatchers.hasExtra(ClassroomsListActivity.EXTRA_ROOM_ID, "CM3"))
        Intents.release()
    }
     */

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) =
            ViewActions.click().perform(uiController, view.findViewById(viewId))
    }
}