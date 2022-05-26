package com.github.sdp.ratemyepfl.ui.fragment.review

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.ui.activity.ReviewActivity
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeCourseRepository
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.serializer.putExtra
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CourseReviewInfoFragmentTest {
    lateinit var scenario: ActivityScenario<ReviewActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @After
    fun clean() {
        scenario.close()
    }

    private fun launch() {
        val reviewable: Reviewable = Course("Fake", "Fake", "Fake", 4, "Fake id", 0.0, 0)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED_ID, "Fake id")
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, reviewable)
        scenario = ActivityScenario.launch(intent)
    }

    @Test
    fun allInformationCorrectlyDisplayed() {
        val fakeCourse = FakeCourseRepository.COURSE_WITH_REVIEW
        FakeCourseRepository.courseById = fakeCourse

        val numReviewText = "(${fakeCourse.numReviews} reviews)"

        launch()

        onView(withId(R.id.courseCode))
            .check(matches(withText(fakeCourse.courseCode)))
        onView(withId(R.id.courseTitle))
            .check(matches(withText(fakeCourse.title)))
        onView(withId(R.id.courseTeacher))
            .check(matches(withText(fakeCourse.teacher)))
        onView(withId(R.id.courseSection))
            .check(matches(withText(fakeCourse.section)))
        onView(withId(R.id.courseCredits))
            .check(matches(withText(fakeCourse.credits.toString())))
        onView(withId(R.id.courseNumReview)).check(matches(withText(numReviewText)))
    }

    @Test
    fun noReviewDisplayed() {
        FakeCourseRepository.courseById = FakeCourseRepository.COURSE_NO_REVIEW

        launch()

        val numReviewText = "(No review submitted)"
        onView(withId(R.id.courseNumReview)).check(matches(withText(numReviewText)))
    }

}