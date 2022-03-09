package com.github.sdp.ratemyepfl

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.ratemyepfl.review.ClassroomsListActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ClassroomsListActivityTest {

    @get:Rule
    val testRule = ActivityScenarioRule(ClassroomsListActivity::class.java)

    @Test
    fun isRecycleViewVisibleOnActivityLaunch() {
        onView(withId(R.id.rooms_recycler_view))
            .check(matches(isDisplayed()))
    }

    @Test
    fun firesAnIntentWhenUserClicksOnReviewButton() {
        init()
        onView(withId(R.id.rooms_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, clickOnViewChild(R.id.reviewRoomButton)
                )
            )
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @Test
    fun hasExtraWhenUserClicksOnReviewButton() {
        init()
        onView(withId(R.id.rooms_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, clickOnViewChild(R.id.reviewRoomButton)
                )
            )

        intended(hasExtra(ClassroomsListActivity.ROOM_ID, "CM3"))
        release()
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) =
            click().perform(uiController, view.findViewById(viewId))
    }

}