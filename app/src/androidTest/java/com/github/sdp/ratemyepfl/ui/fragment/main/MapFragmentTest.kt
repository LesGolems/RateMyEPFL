package com.github.sdp.ratemyepfl.ui.fragment.main

import android.Manifest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeRestaurantRepository
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.utils.UiUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class MapFragmentTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var repository: RestaurantRepositoryImpl

    @Before
    fun setup() {
        hiltAndroidRule.inject()
        runTest {
            repository.add(FakeRestaurantRepository.DEFAULT_RESTAURANT).collect()
        }
    }

    @After
    fun tearDown() {
        runTest { repository.remove(FakeRestaurantRepository.DEFAULT_RESTAURANT.getId()).collect() }
    }

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

    @ExperimentalCoroutinesApi
    @Test
    fun isMapVisibleOnActivityLaunch() {
        HiltUtils.launchFragmentInHiltContainer<MapFragment> {}
        onView(withId(R.id.map)).check(matches(isDisplayed()))
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
        val kebab = UiUtils.objectWithDescription("Roulotte du Soleil")
        kebab.click()
        onView(withId(R.id.map)).check(matches(isDisplayed()))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clickOnMap() {
        HiltUtils.launchFragmentInHiltContainer<MapFragment> {}
        onView(withId(R.id.map)).perform(click())
        onView(withId(R.id.map)).check(matches(isDisplayed()))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clickOnReviewButton() {
        runTest { }
        HiltUtils.launchFragmentInHiltContainer<MapFragment> {}
        init()
        val kebab = UiUtils.objectWithDescription("Roulotte du Soleil")
        kebab.click()
        Thread.sleep(2000)
        onView(withId(R.id.reviewableButton)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clickOnMapAfterMarker() {
        HiltUtils.launchFragmentInHiltContainer<MapFragment> {}
        val kebab = UiUtils.objectWithDescription("Roulotte du Soleil")
        kebab.click()
        onView(withId(R.id.map)).perform(click())
        onView(withId(R.id.map)).check(matches(isDisplayed()))
    }
}