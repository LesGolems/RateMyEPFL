package com.github.sdp.ratemyepfl.fragment.review

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.fakes.FakeClassroomRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeReviewsRepository
import com.github.sdp.ratemyepfl.utils.CustomViewActions
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

    @After
    fun clean() {
        scenario.close()
    }

    private fun launch() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_MENU_ID, R.menu.bottom_navigation_menu_room_review)
        intent.putExtra(ReviewActivity.EXTRA_GRAPH_ID, R.navigation.nav_graph_room_review)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, "Fake id")
        scenario = ActivityScenario.launch(intent)
    }

    @Test
    fun allInformationCorrectlyDisplayed() {
        val fakeRoom = FakeClassroomRepository.ROOM_WITH_REVIEWS
        FakeClassroomRepository.roomById = fakeRoom

        launch()

        val numReviewText = "(${fakeRoom.numReviews} reviews)"
        onView(withId(R.id.roomIdInfo))
            .check(matches(withText(fakeRoom.name)))
        onView(withId(R.id.roomNumReview)).check(matches(withText(numReviewText)))
    }

    @Test
    fun noReviewDisplayed() {
        val fakeRoom = FakeClassroomRepository.ROOM_WITHOUT_REVIEWS
        FakeClassroomRepository.roomById = fakeRoom

        launch()

        // Refresh
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.reviewListFragment))
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.roomReviewInfoFragment))

        val numReviewText = "(No review submitted)"
        onView(withId(R.id.roomNumReview)).check(matches(withText(numReviewText)))
        FakeReviewsRepository.reviewList = FakeReviewsRepository.fakeList
    }

}