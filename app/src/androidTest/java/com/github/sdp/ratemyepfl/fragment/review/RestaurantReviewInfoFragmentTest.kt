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
import com.github.sdp.ratemyepfl.database.fakes.FakeRestaurantRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeReviewsRepository
import com.github.sdp.ratemyepfl.model.serializer.putExtra
import com.github.sdp.ratemyepfl.utils.CustomViewActions
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
        intent.putExtra(
            ReviewActivity.EXTRA_MENU_ID,
            R.menu.bottom_navigation_menu_restaurant_review
        )
        intent.putExtra(ReviewActivity.EXTRA_GRAPH_ID, R.navigation.nav_graph_restaurant_review)
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

        onView(withId(R.id.restaurantIdInfo))
            .check(matches(withText(fakeRestaurant.name)))
        onView(withId(R.id.restaurantNumReview)).check(matches(withText(numReviewText)))
    }

    @Test
    fun noOccupancyDisplayed() {
        FakeRestaurantRepository.restaurantById =
            FakeRestaurantRepository.RESTAURANT_WITH_NO_OCCUPANCY

        launch()

        onView(withId(R.id.occupancyRating))
            .check(matches(withText("Clear")))
    }

    @Test
    fun midOccupancyDisplayed() {
        FakeRestaurantRepository.restaurantById =
            FakeRestaurantRepository.RESTAURANT_WITH_MEDIUM_OCCUPANCY

        launch()

        onView(withId(R.id.occupancyRating))
            .check(matches(withText("Busy")))
    }

    @Test
    fun fullOccupancyDisplayed() {
        FakeRestaurantRepository.restaurantById =
            FakeRestaurantRepository.RESTAURANT_WITH_FULL_OCCUPANCY

        launch()

        onView(withId(R.id.occupancyRating))
            .check(matches(withText("Full")))
    }

    @Test
    fun noReviewDisplayed() {
        FakeRestaurantRepository.restaurantById = FakeRestaurantRepository.RESTAURANT_NO_REVIEWS

        launch()

        // Refresh
        onView(withId(R.id.reviewBottomNavigationView)).perform(CustomViewActions.navigateTo(R.id.reviewListFragment))
        onView(withId(R.id.reviewBottomNavigationView)).perform(CustomViewActions.navigateTo(R.id.restaurantReviewInfoFragment))

        val numReviewText = "(No review submitted)"
        onView(withId(R.id.restaurantNumReview)).check(matches(withText(numReviewText)))
        FakeReviewsRepository.reviewList = FakeReviewsRepository.fakeList
    }

}