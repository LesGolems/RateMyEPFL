package com.github.sdp.ratemyepfl.fragment.review

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AddReviewFragmentTest    /* {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setUp(){
        HiltUtils.launchFragmentInHiltContainer<AddReviewFragment> {}
    }

{

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
}*/