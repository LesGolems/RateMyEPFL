package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.FakeCourseRepository
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.serializer.ItemSerializer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CourseReviewActivityTest {
    lateinit var scenario: ActivityScenario<ReviewActivity>
    private var course = Course("Fake", "Fake", "Fake", 0, "Fake")

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, R.layout.activity_course_review)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, ItemSerializer.serialize(course))
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun clean() {
        scenario.close()
    }


    @Test
    fun isIdVisibleOnActivityLaunch() {
        val fakeCourseId = FakeCourseRepository.COURSE_BY_ID.id
        onView(withId(R.id.id_course_info))
            .check(matches(withText(course.toString())))
    }


}