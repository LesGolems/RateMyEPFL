package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.restaurants.RestaurantReviewActivity
import com.github.sdp.ratemyepfl.database.FakeReviewsRepository
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
    fun fabListenForReviewIfARestaurantIsGiven() {
        val r = Restaurant("Arcadie")
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            RestaurantReviewActivity::class.java
        )
        intent.putExtra(
            RestaurantReviewActivity.EXTRA_RESTAURANT_JSON,
            Json.encodeToString(r)
        )

        val scenario: ActivityScenario<RestaurantReviewActivity> =
            ActivityScenario.launch(intent)

        Intents.init()
        onView(withId(R.id.startReviewFAB)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.toPackage("com.github.sdp.ratemyepfl"))
        Intents.intended(IntentMatchers.hasExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, r.id))
        Intents.release()
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
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            RestaurantReviewActivity::class.java
        )
        val scenario: ActivityScenario<RestaurantReviewActivity> =
            ActivityScenario.launch(intent)
        FakeReviewsRepository.reviewList = FakeReviewsRepository.fakeList

        onView(withId(R.id.reviewRecyclerView)).check(matches(ViewMatchers.hasChildCount(1)))
        onView(withId(R.id.swiperefresh)).perform(ViewActions.swipeDown())
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