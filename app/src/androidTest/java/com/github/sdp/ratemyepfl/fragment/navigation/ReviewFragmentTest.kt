package com.github.sdp.ratemyepfl.fragment.navigation

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.viewpager2.widget.ViewPager2
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.FakeClassroomRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @ExperimentalCoroutinesApi
    @Test
    fun loadsClassroomFragmentWhenUsersPressesOnClassroomTab() {
        val scenario =
            HiltUtils.launchFragmentInHiltContainer<ReviewFragment> {}
        onView(withText(ReviewableTabFragment.TAB.CLASSROOM.name)).perform(click())
        checkClassroom()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadsClassroomFragmentWhenUsersSwipeToTheRightClassroomTab() {
        lateinit var fragment: ReviewFragment
        val scenario =
            HiltUtils.launchFragmentInHiltContainer<ReviewFragment>(themeResId = R.style.Theme_RateMyEPFL) {
                fragment = this
            }
    }

    private fun checkClassroom() {
        onView(withText(FakeClassroomRepository.CLASSROOM_LIST[0].toString())).check(
            matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

}