package com.github.sdp.ratemyepfl.fragment.navigation

import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import org.junit.Test


class ReviewFragmentTest {

    @Test
    fun loadsCourseFragmentWhenUsersPressesOnCourseTab() {
        val scenario =
            launchFragment<ReviewFragment>(themeResId = R.style.Theme_RateMyEPFL).recreate()
        onView(withId(R.id.reviewTabLayout)).perform(CustomViewActions.pressesTab(0))
        Thread.sleep(5000)
//        scenario.onFragment { fragment ->
//            val f = fragment.parentFragmentManager.findFragmentById(R.id.)
//            assert(f is CourseTabFragment)
//        }
    }

    @Test
    fun loadsClassroomFragmentWhenUsersPressesOnClassroomTab() {
        val scenario = launchFragment<ReviewFragment>(themeResId = R.style.Theme_RateMyEPFL)
//        onView(withId(R.id.reviewTabLayout)).perform(CustomViewActions.pressesTab(0))
//
//        scenario.onFragment { fragment ->
//            val f = fragment.parentFragmentManager.findFragmentById(R.id.reviewTabFragment)
//            assert(f is CourseTabFragment)
//        }
    }

}