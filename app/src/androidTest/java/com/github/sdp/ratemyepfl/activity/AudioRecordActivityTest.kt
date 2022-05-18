package com.github.sdp.ratemyepfl.activity

import android.Manifest
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.fragment.review.AudioRecordFragment
import com.github.sdp.ratemyepfl.utils.TestUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AudioRecordActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(AudioRecordFragment::class.java)

    @get:Rule(order = 2)
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.RECORD_AUDIO
    )

    @Test
    fun recordButtonIsCorrectlyDisplayedWhenUserStartsRecording() {
        onView(withId(R.id.audioRecordButton)).check(matches(TestUtils.withDrawable(R.drawable.record_circle)))
        onView(withId(R.id.audioRecordButton)).perform(click())
        onView(withId(R.id.audioRecordButton)).check(matches(TestUtils.withDrawable(R.drawable.stop_circle_outline)))
        Thread.sleep(1000)
        onView(withId(R.id.audioRecordButton)).perform(click())
    }

    @Test
    fun noiseIndicatorIsCorrectlyDisplayedWhenUserStartsRecording() {
        onView(withId(R.id.decibelTextView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.noiseTextView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.audioRecordButton)).perform(click())
        onView(withId(R.id.decibelTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.noiseTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun stopRecordWhenUserPausesTheActivity() {
        onView(withId(R.id.audioRecordButton)).perform(click())
        Thread.sleep(1000)
        Espresso.pressBackUnconditionally()
    }

}