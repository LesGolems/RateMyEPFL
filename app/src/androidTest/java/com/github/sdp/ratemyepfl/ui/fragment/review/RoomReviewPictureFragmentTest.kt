package com.github.sdp.ratemyepfl.ui.fragment.review

import android.Manifest
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeImageStorage
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.serializer.putExtra
import com.github.sdp.ratemyepfl.ui.activity.ReviewActivity
import com.github.sdp.ratemyepfl.ui.adapter.RoomPictureAdapter
import com.github.sdp.ratemyepfl.utils.CustomViewActions.TabAction.selectTabAtPosition
import com.github.sdp.ratemyepfl.utils.TestUtils.createImageGallerySetResultStub
import com.github.sdp.ratemyepfl.utils.TestUtils.getActivity
import com.github.sdp.ratemyepfl.utils.TestUtils.savePickedImage
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


    @get:Rule(order = 1)
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @Before
    fun setUp() {
        val reviewable = Classroom("Fake id", 0.0, 0)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, reviewable)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED_ID, "Fake id")
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.reviewTabLayout)).perform(selectTabAtPosition(2))
        Thread.sleep(1500)
    }

    @After
    fun clean() {
        scenario.close()
    }

    @Test
    fun imageGridIsVisible() {
        onView(withId(R.id.pictureRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun imageGridSizeIsCorrect() {
        onView(withId(R.id.pictureRecyclerView)).check(
            matches(hasChildCount(FakeImageStorage.pictures.size))
        )
    }

    @Test
    fun firesAnIntentWhenUserClicksOnAnImage() {
        init()
        onView(withId(R.id.pictureRecyclerView)).perform(
            actionOnItemAtPosition<RoomPictureAdapter.RoomPictureViewHolder>(0, click())
        )
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @Test
    fun selectPhotoWorks() {
        init()
        val activity = getActivity(scenario)
        savePickedImage(activity, R.raw.pp1)
        val imgGalleryResult = createImageGallerySetResultStub(activity)
        intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(imgGalleryResult)
        onView(withId(R.id.selectPhotoFAB)).perform(click())
        release()
    }

    @Test
    fun startCamera() {
        onView(withId(R.id.capturePhotoFAB)).perform(click())
    }
}