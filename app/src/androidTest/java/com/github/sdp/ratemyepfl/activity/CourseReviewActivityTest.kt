package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.FakeItemsRepository
import com.github.sdp.ratemyepfl.model.items.Course
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CourseReviewActivityTest {
    private lateinit var scenario: ActivityScenario<ReviewActivity>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp(){
        FakeItemsRepository.fakeItem = Course("SDP", "IN", "George", 4, "Fake id")
        val intent = Intent(ApplicationProvider.getApplicationContext(), ReviewActivity::class.java)
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun clean(){
        scenario.close()
    }

    @Test
    fun isIdVisibleOnActivityLaunch() {
        onView(withId(R.id.id_course_info))
            .check(matches(ViewMatchers.withText("Fake id")))
    }
}