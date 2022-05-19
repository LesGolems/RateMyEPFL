package com.github.sdp.ratemyepfl.fragment.review

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.fakes.FakeClassroomRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeReviewsRepository
import com.github.sdp.ratemyepfl.model.serializer.putExtra
import com.github.sdp.ratemyepfl.utils.CustomViewActions.ViewPagerAction
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RoomReviewInfoFragmentTest {
    lateinit var scenario: ActivityScenario<ReviewActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.RECORD_AUDIO
    )

    @After
    fun clean() {
        scenario.close()
    }

    private fun launch() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED_ID, "Fake id")
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, FakeClassroomRepository.CLASSROOM_LIST.first())
        scenario = ActivityScenario.launch(intent)
    }

    @Test
    fun audioReturnsDisplayed(){
        init()
        launch()
        val intent = Intent()
        intent.putExtra("com.github.sdp.extra_measurement_value", 50)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, intent)
        intending(toPackage("com.github.sdp.ratemyepfl")).respondWith(result)
        onView(withId(R.id.noiseMeasureButton)).perform(click())

        ViewPagerAction.swipeNext()
        ViewPagerAction.swipePrevious()
        Thread.sleep(1000)

        val dbText = "Calm (50 dB) - 0 minutes ago"
        onView(withId(R.id.roomNoiseInfoTextView)).check(matches(withText(dbText)))
        release()
    }

    @Test
    fun allInformationCorrectlyDisplayed() {
        val fakeRoom = FakeClassroomRepository.ROOM_WITH_REVIEW
        FakeClassroomRepository.roomById = fakeRoom

        launch()

        val numReviewText = "(${fakeRoom.numReviews} reviews)"
        onView(withId(R.id.roomIdInfo))
            .check(matches(withText(fakeRoom.name)))
        onView(withId(R.id.roomNumReview)).check(matches(withText(numReviewText)))
    }

    @Test
    fun noReviewDisplayed() {
        FakeClassroomRepository.roomById = FakeClassroomRepository.ROOM_NO_REVIEW

        launch()

        val numReviewText = "(No review submitted)"
        onView(withId(R.id.roomNumReview)).check(matches(withText(numReviewText)))
        FakeReviewsRepository.reviewList = FakeReviewsRepository.fakeList
    }

    @Test
    fun firesAnIntentWhenUserClicksOnMicrophone() {
        launch()
        init()
        onView(withId(R.id.noiseMeasureButton)).perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

}