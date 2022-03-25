package com.github.sdp.ratemyepfl.activity

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.restaurants.RestaurantListActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RestaurantListActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(RestaurantListActivity::class.java)

//    @Test
//    fun isRecyclerViewVisibleOnActivityLaunch() {
//        Espresso.onView(ViewMatchers.withId(R.id.reviewableRecyclerView))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//    }
//
//    @Test
//    fun firesAnIntentWhenUserClicksOnReviewButton() {
//        Intents.init()
//        Espresso.onView(ViewMatchers.withId(R.id.reviewableRecyclerView))
//            .perform(
//                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                    0, clickOnViewChild(R.id.reviewableButton)
//                )
//            )
//        Intents.intended(IntentMatchers.toPackage("com.github.sdp.ratemyepfl"))
//        Intents.release()
//    }
//
//    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
//        override fun getConstraints() = null
//
//        override fun getDescription() = "Click on a child view with specified id."
//
//        override fun perform(uiController: UiController, view: View) =
//            ViewActions.click().perform(uiController, view.findViewById(viewId))
//    }
}