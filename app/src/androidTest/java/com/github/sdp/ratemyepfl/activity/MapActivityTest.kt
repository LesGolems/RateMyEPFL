package com.github.sdp.ratemyepfl.activity

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.activity.map.MapActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule

@HiltAndroidTest
class MapActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(MapActivity::class.java)

    /*
    @Test
    fun isMapVisibleOnActivityLaunch() {
        onView(withId(R.id.map))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun changesTypeCorrectlyWhenUserClicksOnStandard() {
        onView(withOverflowMenuItemText(R.string.change_map_type)).perform(click())
        onView(withMenuItemText(R.string.standard_type)).perform(click())
        onView(withId(R.id.map))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun changesTypeCorrectlyWhenUserClicksOnSatellite() {
        onView(withOverflowMenuItemText(R.string.change_map_type)).perform(click())
        onView(withMenuItemText(R.string.satellite_type)).perform(click())
        onView(withId(R.id.map))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun changesTypeCorrectlyWhenUserClicksOnHybrid() {
        onView(withOverflowMenuItemText(R.string.change_map_type)).perform(click())
        onView(withMenuItemText(R.string.hybrid_type)).perform(click())
        onView(withId(R.id.map))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun changesTypeCorrectlyWhenUserClicksOnTerrain() {
        onView(withOverflowMenuItemText(R.string.change_map_type)).perform(click())
        onView(withMenuItemText(R.string.terrain_type)).perform(click())
        onView(withId(R.id.map))
            .check(ViewAssertions.matches(isDisplayed()))
    }
     */
}