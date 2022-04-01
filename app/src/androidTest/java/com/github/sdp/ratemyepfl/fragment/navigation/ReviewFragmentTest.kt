package com.github.sdp.ratemyepfl.fragment.navigation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.FakeClassroomRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ReviewFragmentTest {
    @get:Rule
    val hiltAndroidRule: HiltAndroidRule = HiltAndroidRule(this)

    @Test
    fun loadsCourseFragmentWhenUsersPressesOnCourseTab() {
        //onView(withId(R.id.reviewTabLayout)).perform(CustomViewActions.pressesTab(0))
//        scenario.onFragment { fragment ->
//            val f = fragment.parentFragmentManager.findFragmentById(R.id.)
//            assert(f is CourseTabFragment)
//        }
    }

    @Test
    fun loadsClassroomFragmentWhenUsersPressesOnClassroomTab() {
        lateinit var fragment: ReviewFragment
        val scenario =
            HiltUtils.launchFragmentInHiltContainer<ReviewFragment>(themeResId = R.style.Theme_RateMyEPFL) {
                fragment = this
            }
        onView(withText(ReviewableTabFragment.CLASSROOM_TAB_NAME)).perform(click())
        var str: String? = null
        onView(withId(R.id.reviewableRecyclerView)).perform(CustomViewActions.getInRecyclerView(0) { data ->
            str = data
        })
        assertEquals(FakeClassroomRepository.CLASSROOM_LIST[0].toString(), str)
    }

}