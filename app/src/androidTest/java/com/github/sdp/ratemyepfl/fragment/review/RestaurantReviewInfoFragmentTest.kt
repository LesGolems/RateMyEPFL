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
import com.github.sdp.ratemyepfl.database.FakeRestaurantRepository
import com.github.sdp.ratemyepfl.database.FakeReviewsRepository
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RestaurantReviewInfoFragmentTest {
    lateinit var scenario: ActivityScenario<ReviewActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, R.layout.activity_restaurant_review)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, "Fake id")
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun clean() {
        scenario.close()
    }

    @Test
    fun allInformationCorrectlyDisplayed() {
        val fakeRestaurant = FakeRestaurantRepository.DEFAULT_RESTAURANT
        val fakeReviewList = FakeReviewsRepository.fakeList
        val numReviewText = "(${fakeReviewList.size} reviews)"
        onView(withId(R.id.restaurantIdInfo))
            .check(matches(withText(fakeRestaurant.id)))
        onView(withId(R.id.restaurantNumReview)).check(matches(withText(numReviewText)))
    }

    @Test
    fun noReviewDisplayed() {
        FakeReviewsRepository.reviewList = listOf()

        // Refresh
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.addReviewFragment))
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.restaurantReviewInfoFragment))

        val numReviewText = "(No review submitted)"
        onView(withId(R.id.restaurantNumReview)).check(matches(withText(numReviewText)))
        FakeReviewsRepository.reviewList = FakeReviewsRepository.fakeList
    }

}