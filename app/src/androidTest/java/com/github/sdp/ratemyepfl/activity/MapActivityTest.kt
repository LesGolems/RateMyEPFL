package com.github.sdp.ratemyepfl.activity

import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.map.MapActivity
import com.github.sdp.ratemyepfl.utils.withMenuItemText
import com.github.sdp.ratemyepfl.utils.withOverflowMenuItemText
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MapActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(MapActivity::class.java)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

    @Test
    fun isMapVisibleOnActivityLaunch() {
        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }

    @Test
    fun changesTypeCorrectlyWhenUserClicksOnStandard() {
        onView(withOverflowMenuItemText(R.string.change_map_type)).perform(click())
        onView(withMenuItemText(R.string.standard_type)).perform(click())
        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }

    @Test
    fun changesTypeCorrectlyWhenUserClicksOnSatellite() {
        onView(withOverflowMenuItemText(R.string.change_map_type)).perform(click())
        onView(withMenuItemText(R.string.satellite_type)).perform(click())
        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }

    @Test
    fun changesTypeCorrectlyWhenUserClicksOnHybrid() {
        onView(withOverflowMenuItemText(R.string.change_map_type)).perform(click())
        onView(withMenuItemText(R.string.hybrid_type)).perform(click())
        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }

    @Test
    fun changesTypeCorrectlyWhenUserClicksOnTerrain() {
        onView(withOverflowMenuItemText(R.string.change_map_type)).perform(click())
        onView(withMenuItemText(R.string.terrain_type)).perform(click())
        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }
}