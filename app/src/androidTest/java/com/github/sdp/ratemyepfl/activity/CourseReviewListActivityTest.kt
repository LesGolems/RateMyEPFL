package com.github.sdp.ratemyepfl.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.course.CourseReviewListActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CourseReviewListActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(CourseReviewListActivity::class.java)

    @Test
    fun isCoursesListViewViewVisibleOnActivityLaunch() {
        onView(withId(R.id.reviewRecyclerView))
            .check(matches(isDisplayed()))
    }

    /*
    @HiltAndroidTest
    class NoRule {
        @get:Rule(order = 0)
        val hiltRule = HiltAndroidRule(this)
        @Test
        fun fabListenForReviewIfACourseIsGiven() {
            val course = Course("Sweng", "CS", "Candea", 4, "CS-306")
            val intent = Intent(ApplicationProvider.getApplicationContext(), CourseReviewListActivity::class.java)
            intent.putExtra(CourseReviewActivity.EXTRA_COURSE_IDENTIFIER, Json.encodeToString(course))

            val scenario: ActivityScenario<CourseReviewListActivity> = ActivityScenario.launch(intent)
            init()
            onView(withId(R.id.startCourseReviewFAB)).perform(click())
            intended(toPackage("com.github.sdp.ratemyepfl.activity.course"))
            release()
            scenario.close()
        }
    }
    */

}