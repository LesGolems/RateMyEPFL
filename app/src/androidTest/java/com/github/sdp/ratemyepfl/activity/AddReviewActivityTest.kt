package com.github.sdp.ratemyepfl.activity

import android.app.Activity
import android.content.Intent
import android.widget.RatingBar
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matcher
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class AddReviewActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(AddReviewActivity::class.java)

    @Test
    fun nullGradeCancelsActivity() {
        init()

        val comment = "Good"
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())


        assertThat(testRule.scenario.result.resultCode, equalTo(Activity.RESULT_CANCELED))

        release()
    }

    @Test
    fun nullCommentCancelsActivity() {
        init()

        onView(withId(R.id.reviewRatingBar)).perform(click())
        onView(withId(R.id.doneButton)).perform(click())

        assertThat(testRule.scenario.result.resultCode, equalTo(Activity.RESULT_CANCELED))

        release()
    }

    @Test
    fun nullTitleCancelsActivity() {
        init()

        val comment = "Good"
        onView(withId(R.id.reviewRatingBar)).perform(performSetRating(ReviewRating.GOOD))
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())

        assertThat(testRule.scenario.result.resultCode, equalTo(Activity.RESULT_CANCELED))

        release()
    }

    @Test
    fun nullReviewableIdCancelsActivity() {
        init()

        val comment = "Good"
        val title = "Good title"
        onView(withId(R.id.reviewRatingBar)).perform(performSetRating(ReviewRating.GOOD))
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.addReviewTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())

        assertThat(testRule.scenario.result.resultCode, equalTo(Activity.RESULT_CANCELED))

        release()
    }

    @Test
    fun nonNullArgumentsGivesOK() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), AddReviewActivity::class.java)
        intent.putExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, "ID")
        val scenario: ActivityScenario<AddReviewActivity> = ActivityScenario.launch(intent)
        init()

        val comment = "Good"
        val title = "Good title"
        onView(withId(R.id.reviewRatingBar)).perform(performSetRating(ReviewRating.GOOD))
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.addReviewTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())

        assertThat(testRule.scenario.result.resultCode, equalTo(Activity.RESULT_OK))
        release()
        scenario.close()
    }

    companion object {
        private fun performSetRating(value: Float) = object : ViewAction {
            override fun getConstraints(): Matcher<android.view.View> {
                return ViewMatchers.isAssignableFrom(RatingBar::class.java)
            }

            override fun getDescription(): String {
                return "Custom view action to set rating."
            }

            override fun perform(uiController: UiController?, view: android.view.View?) {
                val ratingBar = view as RatingBar
                ratingBar.rating = value
            }

        }

        fun performSetRating(rating: ReviewRating) =
            performSetRating(rating.rating.toFloat())
    }
}