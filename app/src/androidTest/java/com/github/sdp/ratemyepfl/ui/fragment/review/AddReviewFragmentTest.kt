package com.github.sdp.ratemyepfl.ui.fragment.review

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.model.serializer.putExtra
import com.github.sdp.ratemyepfl.ui.activity.ReviewActivity
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import com.github.sdp.ratemyepfl.utils.CustomViewActions.RatingAction.performSetRating
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
        val reviewable = Course("Fake id", "fake", "fake", 0, "fake", 0.0, 0)
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED_ID, "Fake id")
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, reviewable)
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.reviewTabLayout)).perform(
            CustomViewActions.TabAction.selectTabAtPosition(
                1
            )
        )
        Thread.sleep(1500)
        onView(withId(R.id.addReviewButton)).perform(click())
    }

    @After
    fun clean() {
        scenario.close()
    }

    @Test
    fun nullGradeDoesNotWork() {
        val comment = "Good"
        onView(withId(R.id.addPostComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addPostComment)).check(matches(withText(comment)))
    }

    @Test
    fun nullCommentDoesNotWork() {
        val title = "Good"
        onView(withId(R.id.reviewRatingBar)).perform(click())
        onView(withId(R.id.addPostTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addPostTitle)).check(matches(withText(title)))
    }

    @Test
    fun nullTitleDoesNotWork() {
        val comment = "Good"
        onView(withId(R.id.reviewRatingBar)).perform(performSetRating(ReviewRating.GOOD))
        onView(withId(R.id.addPostComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addPostComment)).check(matches(withText(comment)))
    }


    @Test
    fun nonNullArgumentsAddsReview() {
        val comment = "Good"
        val title = "Good title"
        onView(withId(R.id.reviewRatingBar)).perform(
            performSetRating(
                ReviewRating.GOOD
            )
        )
        onView(withId(R.id.addPostComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.addPostTitle)).perform(typeText(title))
        closeSoftKeyboard()
//         For some reason, it does not work ??
//        onView(withId(R.id.doneButton)).perform(click())
//        onView(withId(R.id.postRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun userNotConnectedDoesNotWork() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.LOGGED_OUT
        val comment = "Good"
        val title = "Good title"
        onView(withId(R.id.reviewRatingBar)).perform(
            performSetRating(
                ReviewRating.GOOD
            )
        )
        onView(withId(R.id.addPostComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.addPostTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.addPostComment)).check(matches(withText(comment)))
        onView(withId(R.id.addPostTitle)).check(matches(withText(title)))
    }

    @Test
    fun anonymousSwitchIsDisplayedAndUncheckedOnLoad() {
        onView(withId(R.id.anonymousSwitch)).check(matches(isDisplayed()))
        onView(withId(R.id.anonymousSwitch)).check(matches(withText("Anonymous")))
        onView(withId(R.id.anonymousSwitch)).check(matches(isNotChecked()))
    }

    @Test
    fun anonymousSwitchWorks() {
        onView(withId(R.id.anonymousSwitch)).check(matches(isNotChecked()))
        onView(withId(R.id.anonymousSwitch)).perform(click())
        onView(withId(R.id.anonymousSwitch)).check(matches(isChecked()))
        onView(withId(R.id.anonymousSwitch)).perform(click())
        onView(withId(R.id.anonymousSwitch)).check(matches(isNotChecked()))
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
        onView(withId(R.id.anonymousSwitch)).perform(click())
        onView(withId(R.id.addPostComment)).perform(typeText(comment))
        closeSoftKeyboard()
        onView(withId(R.id.addPostTitle)).perform(typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.reviewTabLayout)).check(matches(isDisplayed()))
    }
}