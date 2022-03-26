package com.github.sdp.ratemyepfl.fragment.review

import android.view.View
import android.widget.RatingBar
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AddReviewFragmentTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        HiltUtils.launchFragmentInHiltContainer<AddReviewFragment> {}
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

    companion object {
        private fun performSetRating(value: Float) = object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(RatingBar::class.java)
            }

            override fun getDescription(): String {
                return "Custom view action to set rating."
            }

            override fun perform(uiController: UiController?, view: View?) {
                val ratingBar = view as RatingBar
                ratingBar.rating = value
            }

        }

        fun performSetRating(rating: ReviewRating) =
            performSetRating(rating.rating.toFloat())
    }
}