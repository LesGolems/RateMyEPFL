package com.github.sdp.ratemyepfl.fragment.review

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.adapter.PhotoAdapter
import com.github.sdp.ratemyepfl.database.fakes.FakePictureRepository
import com.github.sdp.ratemyepfl.utils.CustomViewActions.navigateTo
import com.github.sdp.ratemyepfl.utils.TestUtils.withDrawable
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RoomReviewPictureFragmentTest {
    lateinit var scenario: ActivityScenario<ReviewActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, R.layout.activity_room_review)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, "Fake id")
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.reviewNavigationView)).perform(navigateTo(R.id.roomReviewPictureFragment))
    }

    @After
    fun clean() {
        scenario.close()
    }

    @Test
    fun imageGridIsVisible() {
        onView(withId(R.id.photoRecyclerView)).check(matches(isDisplayed()))
        scenario.close()
    }

    @Test
    fun imageGridSizeIsCorrect() {
        onView(withId(R.id.photoRecyclerView)).check(
            matches(hasChildCount(FakePictureRepository.PHOTO_LIST.size))
        )
    }

    @Test
    fun imageGridIsCorrectlyDisplayed() {
        for (id: Int in FakePictureRepository.PHOTO_LIST) {
            onView(withId(R.id.photoRecyclerView)).check(
                matches(
                    hasDescendant(withDrawable(id))
                )
            )
        }
    }

    @Test
    fun firesAnIntentWhenUserClicksOnAnImage() {
        init()
        onView(withId(R.id.photoRecyclerView)).perform(
            actionOnItemAtPosition<PhotoAdapter.PhotoViewHolder>(0, click())
        )
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

}