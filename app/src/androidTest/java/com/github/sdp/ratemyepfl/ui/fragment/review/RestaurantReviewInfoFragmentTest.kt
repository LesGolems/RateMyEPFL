package com.github.sdp.ratemyepfl.ui.fragment.review

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeRestaurantRepository
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeReviewRepository
import com.github.sdp.ratemyepfl.model.serializer.putExtra
import com.github.sdp.ratemyepfl.ui.activity.ReviewActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RestaurantReviewInfoFragmentTest {
    lateinit var scenario: ActivityScenario<ReviewActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @After
    fun clean() {
        scenario.close()
    }

    private fun launch() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED_ID, "Fake id")
        intent.putExtra(
            ReviewActivity.EXTRA_ITEM_REVIEWED,
            FakeRestaurantRepository.DEFAULT_RESTAURANT
        )
        scenario = ActivityScenario.launch(intent)
    }

    @Test
    fun allInformationCorrectlyDisplayed() {
        val fakeRestaurant = FakeRestaurantRepository.RESTAURANT_WITH_REVIEWS
        FakeRestaurantRepository.restaurantById = fakeRestaurant

        val numReviewText = "(${fakeRestaurant.numReviews} reviews)"

        launch()

        onView(withId(R.id.restaurantName))
            .check(matches(withText(fakeRestaurant.name)))
        onView(withId(R.id.restaurantNumReview)).check(matches(withText(numReviewText)))
    }

    @Test
    fun noOccupancyDisplayed() {
        FakeRestaurantRepository.restaurantById =
            FakeRestaurantRepository.RESTAURANT_WITH_NO_OCCUPANCY

        launch()

        onView(withId(R.id.occupancyRating))
            .check(matches(withText("CLEAR")))
    }

    @Test
    fun midOccupancyDisplayed() {
        FakeRestaurantRepository.restaurantById =
            FakeRestaurantRepository.RESTAURANT_WITH_MEDIUM_OCCUPANCY

        launch()

        onView(withId(R.id.occupancyRating))
            .check(matches(withText("BUSY")))
    }

    @Test
    fun fullOccupancyDisplayed() {
        FakeRestaurantRepository.restaurantById =
            FakeRestaurantRepository.RESTAURANT_WITH_FULL_OCCUPANCY

        launch()

        onView(withId(R.id.occupancyRating))
            .check(matches(withText("FULL")))
    }

    @Test
    fun noReviewDisplayed() {
        FakeRestaurantRepository.restaurantById = FakeRestaurantRepository.RESTAURANT_NO_REVIEWS

        launch()

        val numReviewText = "(No review submitted)"
        onView(withId(R.id.restaurantNumReview)).check(matches(withText(numReviewText)))
    }

}