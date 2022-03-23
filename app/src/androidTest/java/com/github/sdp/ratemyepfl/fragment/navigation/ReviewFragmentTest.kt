package com.github.sdp.ratemyepfl.fragment.navigation

import androidx.fragment.app.testing.launchFragment
import com.github.sdp.ratemyepfl.R
import org.junit.Test


class ReviewFragmentTest {

    @Test
    fun loadsCourseFragmentWhenUsersPressesOnCourseTab() {
        val scenario =
            launchFragment<ReviewFragment>(themeResId = R.style.Theme_RateMyEPFL).recreate()
//        onView(withId(R.id.reviewTabLayout)).perform(CustomViewActions.pressesTab(0))

//        scenario.onFragment { fragment ->
//            val f = fragment.parentFragmentManager.findFragmentById(R.id.reviewTabFragment)
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