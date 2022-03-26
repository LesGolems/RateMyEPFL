package com.github.sdp.ratemyepfl.activity

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.classrooms.RoomReviewActivity
import com.github.sdp.ratemyepfl.fragment.review.AddReviewFragmentTest
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class RoomReviewActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(RoomReviewActivity::class.java)

    @Test
    fun isIdVisibleOnActivityLaunch() {
        onView(withId(R.id.id_room_info))
            .check(matches(withText("Fake id")))
    }

    /* This tests depends on the Review activity layout, so it doesn't work when launching
       a fragment in isolation
     */
    @Test
    fun nonNullArgumentsResetsAddReview() {
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.addReviewFragment))
        val comment = "Good"
        val title = "Good title"
        onView(withId(R.id.reviewRatingBar)).perform(
            AddReviewFragmentTest.performSetRating(
                ReviewRating.GOOD
            )
        )
        onView(withId(R.id.addReviewComment)).perform(ViewActions.typeText(comment))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.addReviewTitle)).perform(ViewActions.typeText(title))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(ViewActions.click())
        onView(withId(R.id.addReviewComment)).check(matches(withText("")))
        onView(withId(R.id.addReviewTitle)).check(matches(withText("")))
    }
}