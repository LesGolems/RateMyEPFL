package com.github.sdp.ratemyepfl.activity

import android.os.Build
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
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

    /*
    Other way of having correct permissions, but we will miss some branches :(
    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
    GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)*
    */

    @Test
    fun isMapVisibleOnActivityLaunch() {
        grantPermission()
        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }

    @Test
    fun changesTypeCorrectlyWhenUserClicksOnStandard() {
        grantPermission()
        onView(withOverflowMenuItemText(R.string.change_map_type)).perform(click())
        onView(withMenuItemText(R.string.standard_type)).perform(click())
        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }

    @Test
    fun changesTypeCorrectlyWhenUserClicksOnSatellite() {
        grantPermission()
        onView(withOverflowMenuItemText(R.string.change_map_type)).perform(click())
        onView(withMenuItemText(R.string.satellite_type)).perform(click())
        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }

    @Test
    fun changesTypeCorrectlyWhenUserClicksOnHybrid() {
        grantPermission()
        onView(withOverflowMenuItemText(R.string.change_map_type)).perform(click())
        onView(withMenuItemText(R.string.hybrid_type)).perform(click())
        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }

    @Test
    fun changesTypeCorrectlyWhenUserClicksOnTerrain() {
        grantPermission()
        onView(withOverflowMenuItemText(R.string.change_map_type)).perform(click())
        onView(withMenuItemText(R.string.terrain_type)).perform(click())
        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickOnMarker() {
        grantPermission()
        val uiDevice = UiDevice.getInstance(getInstrumentation())
        val kebab = uiDevice.findObject(
            UiSelector()
                .descriptionContains("Roulotte du Soleil")
        )
        kebab.click()
        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }

    private fun grantPermission() {
        val instrumentation = getInstrumentation()
        if (Build.VERSION.SDK_INT >= 23) {
            val allowPermission = UiDevice.getInstance(instrumentation).findObject(
                UiSelector().text(
                    when {
                        Build.VERSION.SDK_INT == 23 -> "Allow"
                        Build.VERSION.SDK_INT <= 28 -> "ALLOW"
                        Build.VERSION.SDK_INT == 29 -> "Allow only while using the app"
                        else -> "While using the app"
                    }
                )
            )
            if (allowPermission.exists()) {
                allowPermission.click()
            }
        }
    }

    private fun denyPermission() {
        val instrumentation = getInstrumentation()
        if (Build.VERSION.SDK_INT >= 23) {
            val denyPermission = UiDevice.getInstance(instrumentation).findObject(
                UiSelector().text(
                    when (Build.VERSION.SDK_INT) {
                        in 24..28 -> "DENY"
                        else -> "Deny"
                    }
                )
            )
            if (denyPermission.exists()) {
                denyPermission.click()
            }
        }
    }
}