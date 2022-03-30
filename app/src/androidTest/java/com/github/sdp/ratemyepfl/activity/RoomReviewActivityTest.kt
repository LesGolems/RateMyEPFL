package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.fragment.review.AddReviewFragmentTest
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.github.sdp.ratemyepfl.model.serializer.ItemSerializer
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class RoomReviewActivityTest {
    lateinit var scenario: ActivityScenario<ReviewActivity>
    private val classroom = Classroom("Fake")

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, R.layout.activity_room_review)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, ItemSerializer.serialize(classroom))
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun clean() {
        scenario.close()
    }

    @Test
    fun isIdVisibleOnActivityLaunch() {
        onView(withId(R.id.id_room_info))
            .check(matches(withText(classroom.toString())))
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
        closeSoftKeyboard()
        onView(withId(R.id.addReviewTitle)).perform(ViewActions.typeText(title))
        closeSoftKeyboard()
        onView(withId(R.id.doneButton)).perform(ViewActions.click())
        onView(withId(R.id.addReviewComment)).check(matches(withText("")))
        onView(withId(R.id.addReviewTitle)).check(matches(withText("")))
    }
}