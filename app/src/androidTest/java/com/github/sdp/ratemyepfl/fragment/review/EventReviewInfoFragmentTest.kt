package com.github.sdp.ratemyepfl.fragment.review

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.fakes.FakeEventRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeGradeInfoRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeReviewsRepository
import com.github.sdp.ratemyepfl.database.reviewable.EventRepository
import com.github.sdp.ratemyepfl.database.reviewable.EventRepositoryImpl
import com.github.sdp.ratemyepfl.model.GradeInfo
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.serializer.putExtra
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class EventReviewInfoFragmentTest {

    val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        .putExtra(ReviewActivity.EXTRA_MENU_ID, R.menu.bottom_navigation_menu_event_review)
        .putExtra(ReviewActivity.EXTRA_GRAPH_ID, R.navigation.nav_graph_event_review)
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
        val fakeEvent = FakeEventRepository.eventById
        val gi = GradeInfo("id", mapOf(), 4.5, 5)
        FakeGradeInfoRepository.gradeById = gi

        launch()

        val numReviewText = "(${gi.numReviews} reviews)"
        onView(withId(R.id.eventIdInfo))
            .check(matches(withText(fakeEvent.name)))
        onView(withId(R.id.eventNumReview)).check(matches(withText(numReviewText)))
    }

    @Test
    fun noReviewDisplayed() {
        FakeGradeInfoRepository.gradeById = FakeGradeInfoRepository.NO_REVIEW

        launch()

        // Refresh
        onView(withId(R.id.reviewBottomNavigationView)).perform(CustomViewActions.navigateTo(R.id.reviewListFragment))
        onView(withId(R.id.reviewBottomNavigationView)).perform(CustomViewActions.navigateTo(R.id.eventReviewInfoFragment))

        val numReviewText = "(No review submitted)"
        onView(withId(R.id.eventNumReview)).check(matches(withText(numReviewText)))
        FakeReviewsRepository.reviewList = FakeReviewsRepository.fakeList
    }

}