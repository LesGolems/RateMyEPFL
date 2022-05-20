package com.github.sdp.ratemyepfl.fragment.review

import android.Manifest
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.database.fakes.FakeRoomNoiseRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.serializer.putExtra
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import com.github.sdp.ratemyepfl.utils.TestUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AudioRecordFragmentTest {
    lateinit var scenario: ActivityScenario<ReviewActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.RECORD_AUDIO
    )

    @Before
    fun setUp() {
        val reviewable = Classroom("Fake id", 0.0, 0)
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        FakeRoomNoiseRepository.measureInfo = FakeRoomNoiseRepository.WITH_MEASURE
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(
            ReviewActivity.EXTRA_MENU_ID,
            R.menu.bottom_navigation_menu_room_review
        )
        intent.putExtra(ReviewActivity.EXTRA_GRAPH_ID, R.navigation.nav_graph_room_review)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED_ID, "Fake id")
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, reviewable)
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.noiseMeasureButton)).perform(click())
    }

    @After
    fun clean() {
        scenario.close()
    }

    @Test
    fun recordButtonIsCorrectlyDisplayedWhenUserStartsRecording() {
        onView(withId(R.id.popUpButton)).perform(click())
        onView(withId(R.id.audioRecordButton)).check(matches(TestUtils.withDrawable(R.drawable.record_circle)))
        onView(withId(R.id.audioRecordButton)).perform(click())
        onView(withId(R.id.audioRecordButton)).check(matches(TestUtils.withDrawable(R.drawable.stop_circle_outline)))
        Thread.sleep(2000)
        onView(withId(R.id.audioRecordButton)).perform(click())
    }
}