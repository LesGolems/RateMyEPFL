package com.github.sdp.ratemyepfl.fragment.review

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import com.github.sdp.ratemyepfl.utils.CustomViewActions.RatingAction.performSetRating
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AddEventReviewFragmentTest {
    lateinit var scenario: ActivityScenario<ReviewActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, R.layout.activity_event_review)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, "Fake id")
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.addEventReviewFragment))
    }

    @After
    fun clean() {
        scenario.close()
    }

    @Test
    fun nullGradeNoReset() {
        val comment = "Good"
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addReviewComment)).check(matches(withText(comment)))
    }

    @Test
    fun nullCommentNoReset() {
        val title = "Good"
        onView(withId(R.id.reviewRatingBar)).perform(click())
        onView(withId(R.id.addReviewTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addReviewTitle)).check(matches(withText(title)))
    }

    @Test
    fun nullTitleNoReset() {
        val comment = "Good"
        onView(withId(R.id.reviewRatingBar)).perform(performSetRating(ReviewRating.GOOD))
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addReviewComment)).check(matches(withText(comment)))
    }


    @Test
    fun nonNullArgumentsResetsAddReview() {
        val comment = "Good"
        val title = "Good title"
        onView(withId(R.id.reviewRatingBar)).perform(
            performSetRating(
                ReviewRating.GOOD
            )
        )
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.addReviewTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addReviewComment)).check(matches(withText("")))
        onView(withId(R.id.addReviewTitle)).check(matches(withText("")))
    }

    @Test
    fun userNotConnectedNoReset() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.addEventReviewFragment))
        val comment = "Good"
        val title = "Good title"
        onView(withId(R.id.reviewRatingBar)).perform(
            performSetRating(
                ReviewRating.GOOD
            )
        )
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.addReviewTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addReviewComment)).check(matches(withText(comment)))
        onView(withId(R.id.addReviewTitle)).check(matches(withText(title)))
    }

    @Test
    fun anonymousSwitchIsDisplayedAndUncheckedOnLoad() {
        onView(withId(R.id.anonymous_switch)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.anonymous_switch)).check(matches(withText("Anonymous")))
        onView(withId(R.id.anonymous_switch)).check(matches(ViewMatchers.isNotChecked()))
    }

    @Test
    fun anonymousSwitchWorks() {
        onView(withId(R.id.anonymous_switch)).check(matches(ViewMatchers.isNotChecked()))
        onView(withId(R.id.anonymous_switch)).perform(click())
        onView(withId(R.id.anonymous_switch)).check(matches(ViewMatchers.isChecked()))
        onView(withId(R.id.anonymous_switch)).perform(click())
        onView(withId(R.id.anonymous_switch)).check(matches(ViewMatchers.isNotChecked()))
    }

    @Test
    fun anonymousReviewWorks() {
        val comment = "Good"
        val title = "Good title"
        onView(withId(R.id.reviewRatingBar)).perform(
            performSetRating(
                ReviewRating.GOOD
            )
        )
        onView(withId(R.id.anonymous_switch)).perform(click())
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.addReviewTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addReviewComment)).check(matches(withText("")))
        onView(withId(R.id.addReviewTitle)).check(matches(withText("")))
    }
}