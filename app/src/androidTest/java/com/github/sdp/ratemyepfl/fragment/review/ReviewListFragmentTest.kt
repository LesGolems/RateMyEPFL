package com.github.sdp.ratemyepfl.fragment.review

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.FakeReviewsRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@HiltAndroidTest
class ReviewListFragmentTest {
    lateinit var scenario: ActivityScenario<ReviewActivity>

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @Test
    fun listIsVisible() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, R.layout.activity_room_review) // can be any
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, "Fake id")
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.reviewListFragment))
        onView(withId(R.id.reviewRecyclerView))
            .check(ViewAssertions.matches(isDisplayed()))
        scenario.close()
    }

    @Test
    fun swipeRefreshes() {
        FakeReviewsRepository.reviewList = listOf(
            Review.Builder().setTitle("Absolument dé-men-tiel")
                .setComment("Regardez moi cet athlète, regardez moi cette plastique.")
                .setRating(ReviewRating.EXCELLENT)
                .setReviewableID("CS-123")
                .setDate(LocalDate.now())
                .build()
        )
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, R.layout.activity_room_review) // can be any
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, "Fake id")
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.reviewListFragment))
        Thread.sleep(500) // if no wait, the new list is direclty displayed
        FakeReviewsRepository.reviewList = FakeReviewsRepository.fakeList
        onView(withId(R.id.reviewRecyclerView))
            .check(ViewAssertions.matches(hasChildCount(1)))
        onView(withId(R.id.reviewSwipeRefresh)).perform(swipeDown())
        onView(withId(R.id.reviewRecyclerView))
            .check(ViewAssertions.matches(CoreMatchers.not(hasChildCount(1))))
        scenario.close()
    }
}