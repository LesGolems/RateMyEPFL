package com.github.sdp.ratemyepfl.ui.fragment.review

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.ui.activity.ReviewActivity
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeEventRepository
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeReviewRepository
import com.github.sdp.ratemyepfl.model.serializer.putExtra
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class EventReviewInfoFragmentTest {

    val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        .putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED_ID, "Fake id")
        .putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, FakeEventRepository.DEFAULT_EVENT)

    var scenario: ActivityScenario<ReviewActivity>? = null

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @After
    fun clean() {
        scenario?.close()
    }

    fun launch() {
        scenario = ActivityScenario.launch(intent)
    }

    @Test
    fun allInformationCorrectlyDisplayed() {
        val fakeEvent = FakeEventRepository.EVENT_WITH_REVIEW
        FakeEventRepository.eventById = fakeEvent

        launch()

        val numReviewText = "(${fakeEvent.numReviews} reviews)"
        onView(withId(R.id.eventName))
            .check(matches(withText(fakeEvent.name)))
        onView(withId(R.id.eventNumReview)).check(matches(withText(numReviewText)))
    }

    @Test
    fun noReviewDisplayed() {
        FakeEventRepository.eventById = FakeEventRepository.EVENT_NO_REVIEW

        launch()

        val numReviewText = "(No review submitted)"
        onView(withId(R.id.eventNumReview)).check(matches(withText(numReviewText)))
    }

}