package com.github.sdp.ratemyepfl.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.course.CourseListActivity
import com.github.sdp.ratemyepfl.utils.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CourseListActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(CourseListActivity::class.java)

//    @Test
//    fun isRecyclerViewVisibleOnActivityLaunch() {
//        onView(withId(R.id.reviewableRecyclerView))
//            .check(matches(isDisplayed()))
//    }

//    @Test
//    fun firesAnIntentWhenUserClicksOnAnItem() {
//        init()
//        onView(withId(R.id.reviewableRecyclerView))
//            .perform(
//                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                    0, clickOnViewChild(R.id.reviewableId)
//                )
//            )
//        intended(toPackage("com.github.sdp.ratemyepfl"))
//        release()
//    }

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


    /* Tests for search view */
//    @Test
//    fun nonEmptySearchQueryChangesCorrectlyTheList() {
//        openSearchView(R.id.courseSearchView)
//
//        onView(withId(R.id.reviewableRecyclerView))
//            .check(matches(hasDescendant(withText("CS-308 Calcul quantique"))))
//
//        typeQuery("proj")
//        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(pressImeActionButton());
//
//        onView(withId(R.id.reviewableRecyclerView))
//            .check(matches(hasDescendant(withText("CS-306 Software development project"))))
//        onView(withId(R.id.reviewableRecyclerView))
//            .check(matches(not(hasDescendant(withText("CS-308 Calcul quantique")))))
//
//        closeSearchView()
//    }


    /* Tests for overflow menu */
///

//    @Test
//    fun decreasingSort() {
//        onView(withOverflowMenuItemText(R.string.sort_alphabetically)).perform(click())
//        onView(withMenuItemText(R.string.decreasing_order)).perform(click())
//        onView(withId(R.id.reviewableRecyclerView)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun credits() {
//        for (text in R.string.credit_2..R.string.credit_8) {
//            onView(withOverflowMenuItemText(R.string.credits)).perform(click())
//            onView(withMenuItemText(text)).perform(click())
//            onView(withId(R.id.reviewableRecyclerView)).check(matches(isDisplayed()))
//        }
//    }

}