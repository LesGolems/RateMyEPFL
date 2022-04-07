package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MapFragmentTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    /*
    Other way of having correct permissions, but we will miss some branches :(
    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
    GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)*
    */

    @ExperimentalCoroutinesApi
    @Test
    fun isMapVisibleOnActivityLaunch() {
        HiltUtils.launchFragmentInHiltContainer<MapFragment> {}
        grantPermission()
        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }

    /* Redundant for now, but may be useful later
    @ExperimentalCoroutinesApi
    @Test
    fun isMapVisibleIfDenyingPermission() {
        HiltUtils.launchFragmentInHiltContainer<MapFragment> {}
        denyPermission()
        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }
     */

    @ExperimentalCoroutinesApi
    @Test
    fun clickOnMarker() {
        HiltUtils.launchFragmentInHiltContainer<MapFragment> {}
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

    @ExperimentalCoroutinesApi
    @Test
    fun clickOnReviewButton() {
        HiltUtils.launchFragmentInHiltContainer<MapFragment> {}
        init()
        grantPermission()
        val uiDevice = UiDevice.getInstance(getInstrumentation())
        val kebab = uiDevice.findObject(
            UiSelector()
                .descriptionContains("Roulotte du Soleil")
        )
        kebab.click()
        onView(withId(R.id.reviewableButton))
            .perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clickOnMapAfterMarker() {
        HiltUtils.launchFragmentInHiltContainer<MapFragment> {}
        grantPermission()
        val uiDevice = UiDevice.getInstance(getInstrumentation())
        val kebab = uiDevice.findObject(
            UiSelector()
                .descriptionContains("Roulotte du Soleil")
        )
        kebab.click()
        onView(withId(R.id.map))
            .perform(click())
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