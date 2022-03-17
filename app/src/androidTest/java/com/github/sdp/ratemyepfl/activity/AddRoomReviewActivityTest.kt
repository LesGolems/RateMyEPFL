package com.github.sdp.ratemyepfl.activity

import android.app.Activity
import android.widget.RatingBar
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
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.classrooms.AddRoomReviewActivity
import com.github.sdp.ratemyepfl.activity.classrooms.ROOM_COMMENT
import com.github.sdp.ratemyepfl.activity.classrooms.ROOM_GRADE
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.core.View
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
class AddRoomReviewActivityTest {

    @get:Rule (order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule (order = 1)
    val testRule = ActivityScenarioRule(AddRoomReviewActivity::class.java)

    @Test
    fun nullGradeCancelsActivity() {
        init()

        val comment = "Good"
        onView(withId(R.id.add_room_comment)).perform(typeText(comment))
        onView(withId(R.id.done_button)).perform(click())


        assertThat(testRule.scenario.result.resultCode, Matchers.equalTo(Activity.RESULT_CANCELED))

        release()
    }

    @Test
    fun nullCommentCancelsActivity() {
        init()

        onView(withId(R.id.roomReviewRatingBar)).perform(click())
        onView(withId(R.id.done_button)).perform(click())

        assertThat(testRule.scenario.result.resultCode, Matchers.equalTo(Activity.RESULT_CANCELED))

        release()
    }

    @Test
    fun nonNullGradeAndCommentsGivesOK() {
        init()

        val comment = "Good"
        onView(withId(R.id.roomReviewRatingBar)).perform(performSetRating(ReviewRating.GOOD))
        onView(withId(R.id.add_room_comment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.done_button)).perform(click())

        assertThat(testRule.scenario.result.resultCode, Matchers.equalTo(Activity.RESULT_OK))

        release()
    }

    @Test
    fun nonNullGradeAndCommentGivesSameValuesToList() {
        init()

        val rating = ReviewRating.EXCELLENT
        val comment = "Good"
        onView(withId(R.id.roomReviewRatingBar)).perform(
            performSetRating(rating))

        onView(withId(R.id.add_room_comment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.done_button)).perform(click())

        val data = testRule.scenario.result.resultData

        assertThat(testRule.scenario.result.resultCode, Matchers.equalTo(Activity.RESULT_OK))
        assertThat(data.getStringExtra(ROOM_COMMENT), Matchers.equalTo(comment))
        assertThat(data.getIntExtra(ROOM_GRADE, -1), Matchers.equalTo(rating.rating))

        release()
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