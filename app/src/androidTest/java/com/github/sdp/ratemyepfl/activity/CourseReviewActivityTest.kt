package com.github.sdp.ratemyepfl.activity

import android.app.Activity
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.CourseReview
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hamcrest.Matchers
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import java.time.LocalDate


@RunWith(AndroidJUnit4::class)
class CourseReviewActivityTest {

    @Test
    fun nullCourseCancels() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), CourseReviewActivity::class.java)
        val empty : String? = null
        intent.putExtra(CourseReviewActivity.EXTRA_COURSE_IDENTIFIER, empty)

        val scenario: ActivityScenario<CourseReviewActivity> = ActivityScenario.launch(intent)
        assertThat(scenario.result.resultCode, Matchers.equalTo(Activity.RESULT_CANCELED))
        scenario.close()
    }

    @Test
    fun correctReviewWorks() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), CourseReviewActivity::class.java)
        val default = CourseMgtActivity.DEFAULT_COURSE

        intent.putExtra(CourseReviewActivity.EXTRA_COURSE_IDENTIFIER, Json.encodeToString(default))

        val scenario: ActivityScenario<CourseReviewActivity> = ActivityScenario.launch(intent)
        val title = "Title"
        val comment = "Bad"
        // Click on all buttons
        onView(withId(R.id.courseRatingExcellentRadioButton)).perform(click())
        onView(withId(R.id.courseRatingGoodRadioButton)).perform(click())
        onView(withId(R.id.courseRatingAverageRadioButton)).perform(click())
        onView(withId(R.id.courseRatingPoorRadioButton)).perform(click())
        onView(withId(R.id.courseRatingTerribleRadioButton)).perform(click())

        onView(withId(R.id.courseReviewTitle)).perform(typeText(title))
        closeSoftKeyboard()

        onView(withId(R.id.courseReviewOpinion)).perform(typeText(comment))
        closeSoftKeyboard()


        onView(withId(R.id.courseReviewSubmit)).perform(click())

        assertThat(scenario.result.resultCode, Matchers.equalTo(Activity.RESULT_OK))
        assertEquals(scenario.result.resultData.getStringExtra(CourseMgtActivity.EXTRA_COURSE_REVIEWED),
            Json.encodeToString(default))

        val review = CourseReview.Builder()
            .setRating(ReviewRating.TERRIBLE)
            .setTitle(title)
            .setComment(comment)
            .setDate(LocalDate.now())
            .build()

        assertEquals(scenario.result.resultData.getStringExtra(CourseReviewActivity.EXTRA_REVIEW),
            review.serialize())
        scenario.close()
    }

        


}