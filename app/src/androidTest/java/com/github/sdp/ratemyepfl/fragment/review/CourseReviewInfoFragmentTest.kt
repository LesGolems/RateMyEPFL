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
import com.github.sdp.ratemyepfl.database.FakeCourseRepository
import com.github.sdp.ratemyepfl.database.FakeReviewsRepository
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CourseReviewInfoFragmentTest {
    lateinit var scenario: ActivityScenario<ReviewActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, R.layout.activity_course_review)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, "Fake id")
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun clean() {
        scenario.close()
    }

    @Test
    fun allInformationCorrectlyDisplayed() {
        val fakeCourse = FakeCourseRepository.COURSE_BY_ID
        val fakeReviewList = FakeReviewsRepository.fakeList
        val titleText = "Title : ${fakeCourse.title}"
        val teacherText = "Teacher : ${fakeCourse.teacher}"
        val sectionText = "Section : ${fakeCourse.section}"
        val creditsText = "Credits : ${fakeCourse.credits}"
        val numReviewText = "(${fakeReviewList.size} reviews)"
        onView(withId(R.id.courseId))
            .check(matches(withText(fakeCourse.id)))
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
        FakeReviewsRepository.reviewList = listOf()

        // Refresh
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.addReviewFragment))
        onView(withId(R.id.reviewNavigationView)).perform(CustomViewActions.navigateTo(R.id.courseReviewInfoFragment))

        val numReviewText = "(No review submitted)"
        onView(withId(R.id.courseNumReview)).check(matches(withText(numReviewText)))
        FakeReviewsRepository.reviewList = FakeReviewsRepository.fakeList
    }

}