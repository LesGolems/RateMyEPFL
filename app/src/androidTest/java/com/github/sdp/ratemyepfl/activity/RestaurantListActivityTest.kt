package com.github.sdp.ratemyepfl.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.restaurants.RestaurantListActivity
import com.github.sdp.ratemyepfl.utils.closeSearchView
import com.github.sdp.ratemyepfl.utils.openSearchView
import com.github.sdp.ratemyepfl.utils.typeQuery
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers
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
//    fun isSearchViewVisibleOnActivityLaunch() {
//        Espresso.onView(ViewMatchers.withId(R.id.restaurantSearchView))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//    }
//
//    @Test
//    fun firesAnIntentWhenUserClicksOnAnItem() {
//        Intents.init()
//        Espresso.onView(ViewMatchers.withId(R.id.reviewableRecyclerView))
//            .perform(
//                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
//                    0, com.github.sdp.ratemyepfl.utils.clickOnViewChild(R.id.reviewableId)
//                )
//            )
//        Intents.intended(IntentMatchers.toPackage("com.github.sdp.ratemyepfl"))
//        Intents.release()
//    }
//
//    @Test
//    fun nonEmptySearchQueryChangesCorrectlyTheList() {
//        openSearchView(R.id.restaurantSearchView)
//
//        Espresso.onView(ViewMatchers.withId(R.id.reviewableRecyclerView)).check(
//            ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("Takinoa")))
//        )
//
//        // Writes on the view that is opened by clicking on the search view
//        typeQuery("Arcadie")
//
//        Espresso.onView(ViewMatchers.withId(R.id.reviewableRecyclerView)).check(
//            ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("Arcadie")))
//        )
//        Espresso.onView(ViewMatchers.withId(R.id.reviewableRecyclerView)).check(
//            ViewAssertions.matches(
//                CoreMatchers.not(
//                    ViewMatchers.hasDescendant(
//                        ViewMatchers.withText(
//                            "Takinoa"
//                        )
//                    )
//                )
//            )
//        )
//
//        closeSearchView()
//    }
//
//    /*@Test
//    fun noSearchResultsDisplaysNothing() {
//        openSearchView(R.id.searchView)
//        typeQuery("Not a classroom")
//        Espresso.onView(ViewMatchers.withId(R.id.reviewableRecyclerView)).check(
//            ViewAssertions.matches(
//                CoreMatchers.not(
//                    ViewMatchers.hasDescendant(
//                        ViewMatchers.withText(
//                            "CM3"
//                        )
//                    )
//                )
//            )
//        )
//        closeSearchView()
//    }*/
//
//    @Test
//    fun emptySearchQueryDoesNotChangeTheView() {
//        openSearchView(R.id.restaurantSearchView)
//        typeQuery("")
//        Espresso.onView(ViewMatchers.withId(R.id.reviewableRecyclerView)).check(
//            ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("Arcadie")))
//        )
//        closeSearchView()
//    }
//
//    @Test
//    fun increasingSort() {
//        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
//        Espresso.onView(ViewMatchers.withText(R.string.sort_alphabetically))
//            .perform(ViewActions.click())
//        Espresso.onView(ViewMatchers.withText(R.string.increasing_order))
//            .perform(ViewActions.click())
//        Espresso.onView(ViewMatchers.withId(R.id.reviewableRecyclerView))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//    }
//
//    @Test
//    fun decreasingSort() {
//        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
//        Espresso.onView(ViewMatchers.withText(R.string.sort_alphabetically))
//            .perform(ViewActions.click())
//        Espresso.onView(ViewMatchers.withText(R.string.decreasing_order))
//            .perform(ViewActions.click())
//        Espresso.onView(ViewMatchers.withId(R.id.reviewableRecyclerView))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//    }

}