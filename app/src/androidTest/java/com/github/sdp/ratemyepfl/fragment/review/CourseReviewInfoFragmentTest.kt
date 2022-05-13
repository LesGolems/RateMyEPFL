package com.github.sdp.ratemyepfl.fragment.review

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.database.fakes.FakeCourseRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeGradeInfoRepository
import com.github.sdp.ratemyepfl.model.GradeInfo
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.serializer.putExtra
import com.github.sdp.ratemyepfl.utils.CustomViewActions
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
        intent.putExtra(ReviewActivity.EXTRA_MENU_ID, R.menu.bottom_navigation_menu_course_review)
        intent.putExtra(ReviewActivity.EXTRA_GRAPH_ID, R.navigation.nav_graph_course_review)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED_ID, "Fake id")
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, reviewable)
        scenario = ActivityScenario.launch(intent)
    }

    @Test
    fun allInformationCorrectlyDisplayed() {
        val fakeCourse = FakeCourseRepository.COURSE_WITH_REVIEW
        FakeCourseRepository.courseById = fakeCourse

        val titleText = "Title : ${fakeCourse.title}"
        val teacherText = "Teacher : ${fakeCourse.teacher}"
        val sectionText = "Section : ${fakeCourse.section}"
        val creditsText = "Credits : ${fakeCourse.credits}"
        val numReviewText = "(${fakeCourse.numReviews} reviews)"

        launch()

        onView(withId(R.id.courseId))
            .check(matches(withText(fakeCourse.courseCode)))
        onView(withId(R.id.courseTitle))
            .check(matches(withText(titleText)))
        onView(withId(R.id.courseTeacher))
            .check(matches(withText(teacherText)))
        onView(withId(R.id.courseSection))
            .check(matches(withText(sectionText)))
        onView(withId(R.id.courseCredits))
            .check(matches(withText(creditsText)))
        onView(withId(R.id.courseNumReview)).check(matches(withText(numReviewText)))
    }

    @Test
    fun noReviewDisplayed() {
        FakeCourseRepository.courseById = FakeCourseRepository.COURSE_NO_REVIEW

        launch()

        // Refresh
        onView(withId(R.id.reviewBottomNavigationView)).perform(CustomViewActions.navigateTo(R.id.reviewListFragment))
        onView(withId(R.id.reviewBottomNavigationView)).perform(CustomViewActions.navigateTo(R.id.courseReviewInfoFragment))

        val numReviewText = "(No review submitted)"
        onView(withId(R.id.courseNumReview)).check(matches(withText(numReviewText)))
    }

}