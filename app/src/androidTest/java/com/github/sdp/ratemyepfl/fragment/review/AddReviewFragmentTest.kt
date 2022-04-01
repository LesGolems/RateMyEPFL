package com.github.sdp.ratemyepfl.fragment.review

import android.content.Intent
import android.view.View
import android.widget.RatingBar
import androidx.core.os.bundleOf
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AddReviewFragmentTest {
    lateinit var scenario: ActivityScenario<ReviewActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, R.layout.activity_room_review) // can be any
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, "Fake id")
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun clean() {
        scenario.close()
    }

    @Test
    fun nullGradeNoReset() {
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.addReviewFragment))
        val comment = "Good"
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addReviewComment)).check(matches(withText(comment)))
    }

    @Test
    fun nullCommentNoReset() {
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.addReviewFragment))
        val title = "Good"
        onView(withId(R.id.reviewRatingBar)).perform(click())
        onView(withId(R.id.addReviewTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addReviewTitle)).check(matches(withText(title)))
    }

    @Test
    fun nullTitleNoReset() {
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.addReviewFragment))
        val comment = "Good"
        onView(withId(R.id.reviewRatingBar)).perform(performSetRating(ReviewRating.GOOD))
        onView(withId(R.id.addReviewComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addReviewComment)).check(matches(withText(comment)))
    }


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
        closeSoftKeyboard()
        onView(withId(R.id.addReviewTitle)).perform(ViewActions.typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(ViewActions.click())
        onView(withId(R.id.addReviewComment)).check(matches(withText("")))
        onView(withId(R.id.addReviewTitle)).check(matches(withText("")))
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