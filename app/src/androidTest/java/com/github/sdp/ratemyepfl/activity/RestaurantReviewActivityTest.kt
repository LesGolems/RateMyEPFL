package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.restaurants.RestaurantReviewActivity
import com.github.sdp.ratemyepfl.database.FakeReviewsRepository
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@HiltAndroidTest
class RestaurantReviewActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(RestaurantReviewActivity::class.java)

    @Test
    fun isIdVisibleOnActivityLaunch() {
        onView(withId(R.id.id_restaurant_info))
            .check(matches(ViewMatchers.withText("Fake id")))
    }

    @Test
    fun fabListenForReviewIfARestaurantIsGiven() {
        onView(withId(R.id.restaurantReviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.reviewListFragment))
        init()
        onView(withId(R.id.startReviewFAB)).perform(ViewActions.click())
        intended(IntentMatchers.toPackage("com.github.sdp.ratemyepfl"))
        intended(IntentMatchers.hasExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, "Fake id"))
        release()
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
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            RestaurantReviewActivity::class.java
        )
        val scenario: ActivityScenario<RestaurantReviewActivity> =
            ActivityScenario.launch(intent)
        onView(withId(R.id.restaurantReviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.reviewListFragment))
        Thread.sleep(500)
        FakeReviewsRepository.reviewList = FakeReviewsRepository.fakeList
        onView(withId(R.id.reviewRecyclerView)).check(matches(ViewMatchers.hasChildCount(1)))
        onView(withId(R.id.reviewSwipeRefresh)).perform(ViewActions.swipeDown())
        onView(withId(R.id.reviewRecyclerView)).check(matches(
            CoreMatchers.not(
                ViewMatchers.hasChildCount(
                    1
                )
            )
        ))
        scenario.close()
    }
}