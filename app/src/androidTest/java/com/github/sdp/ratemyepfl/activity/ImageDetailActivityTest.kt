package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.utils.CustomViewActions.FingerGestureActions.pinchIn
import com.github.sdp.ratemyepfl.utils.CustomViewActions.FingerGestureActions.pinchOut
import com.github.sdp.ratemyepfl.utils.TestUtils.resourceToBitmap
import com.github.sdp.ratemyepfl.utils.TestUtils.withDrawable
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ImageDetailActivityTest {
    lateinit var scenario: ActivityScenario<ImageDetailActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(ImageDetailActivity::class.java)

    private val pictureId = R.raw.room1
    private val picture = ImageFile("fake Id", resourceToBitmap(pictureId))


    @Before
    fun setUp() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), ImageDetailActivity::class.java)
        ImageDetailActivity.pictureDisplayed = picture.data
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun clean() {
        scenario.close()
    }

    @Test
    fun imageIsVisible() {
        onView(withId(R.id.detailImageView)).check(matches(isDisplayed()))
    }

    @Test
    fun imageDisplayedIsTheCorrectOne() {
        onView(withId(R.id.detailImageView)).check(matches(withDrawable(pictureId)))
    }

    @Test
    fun imageIsZoomedInWhenUserPinchesOut() {
        onView(withId(R.id.detailImageView)).perform(pinchOut())
    }

    @Test
    fun imageIsZoomedOutWhenUserPinchesIn() {
        onView(withId(R.id.detailImageView)).perform(pinchIn())
    }

}