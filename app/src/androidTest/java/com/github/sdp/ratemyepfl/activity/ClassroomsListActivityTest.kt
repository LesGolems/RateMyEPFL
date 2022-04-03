package com.github.sdp.ratemyepfl.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.classrooms.ClassroomsListActivity
import com.github.sdp.ratemyepfl.utils.clickOnViewChild
import com.github.sdp.ratemyepfl.utils.closeSearchView
import com.github.sdp.ratemyepfl.utils.openSearchView
import com.github.sdp.ratemyepfl.utils.typeQuery
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class ClassroomsListActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(ClassroomsListActivity::class.java)

//    @Test
//    fun isRecycleViewVisibleOnActivityLaunch() {
//        onView(withId(R.id.reviewableRecyclerView))
//            .check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun isSearchViewVisibleOnActivityLaunch() {
//        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
//    }
//
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
//
//    @Test
//    fun nonEmptySearchQueryChangesCorrectlyTheList() {
//        openSearchView(R.id.searchView)
//
//        onView(withId(R.id.reviewableRecyclerView)).check(
//            matches(hasDescendant(withText("ELA 2")))
//        )
//
//        // Writes on the view that is opened by clicking on the search view
//        typeQuery("CM3")
//
//        onView(withId(R.id.reviewableRecyclerView)).check(
//            matches(hasDescendant(withText("CM3")))
//        )
//        onView(withId(R.id.reviewableRecyclerView)).check(
//            matches(not(hasDescendant(withText("ELA 2"))))
//        )
//
//        closeSearchView()
//    }
//
//    @Test
//    fun noSearchResultsDisplaysNothing() {
//        openSearchView(R.id.searchView)
//        typeQuery("Not a classroom")
//        onView(withId(R.id.reviewableRecyclerView)).check(
//            matches(not(hasDescendant(withText("CM3"))))
//        )
//        closeSearchView()
//    }
//
//    @Test
//    fun emptySearchQueryDoesNotChangeTheView() {
//        openSearchView(R.id.searchView)
//        typeQuery("")
//        onView(withId(R.id.reviewableRecyclerView)).check(
//            matches(hasDescendant(withText("CM3")))
//        )
//        closeSearchView()
//    }
//
//    @Test
//    fun increasingSort() {
//        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
//        onView(withText(R.string.sort_alphabetically)).perform(click())
//        onView(withText(R.string.increasing_order)).perform(click())
//        onView(withId(R.id.reviewableRecyclerView)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun decreasingSort() {
//        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
//        onView(withText(R.string.sort_alphabetically)).perform(click())
//        onView(withText(R.string.decreasing_order)).perform(click())
//        onView(withId(R.id.reviewableRecyclerView)).check(matches(isDisplayed()))
//    }
}