package com.github.sdp.ratemyepfl.fragment.review

import android.Manifest
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.fakes.FakeClassroomRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeReviewRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeRoomNoiseRepository
import com.github.sdp.ratemyepfl.model.serializer.putExtra
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
        intent.putExtra(ReviewActivity.EXTRA_MENU_ID, R.menu.bottom_navigation_menu_room_review)
        intent.putExtra(ReviewActivity.EXTRA_GRAPH_ID, R.navigation.nav_graph_room_review)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED_ID, "Fake id")
        intent.putExtra(
            ReviewActivity.EXTRA_ITEM_REVIEWED,
            FakeClassroomRepository.CLASSROOM_LIST.first()
        )
        scenario = ActivityScenario.launch(intent)
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

        // Refresh
        onView(withId(R.id.reviewBottomNavigationView)).perform(CustomViewActions.navigateTo(R.id.reviewListFragment))
        onView(withId(R.id.reviewBottomNavigationView)).perform(CustomViewActions.navigateTo(R.id.roomReviewInfoFragment))

        val numReviewText = "(No review submitted)"
        onView(withId(R.id.roomNumReview)).check(matches(withText(numReviewText)))
        FakeReviewRepository.reviewList = FakeReviewRepository.fakeList
    }

    @Test
    fun displaysCorrectFragmentWhenUserClicksOnMicrophone() {
        FakeRoomNoiseRepository.measureInfo = FakeRoomNoiseRepository.WITH_MEASURE
        launch()
        init()
        onView(withId(R.id.noiseMeasureButton)).perform(click())
        onView(withId(R.id.recordRecyclerView)).check(matches(ViewMatchers.isDisplayed()))
        release()
    }

}