package com.github.sdp.ratemyepfl.fragment.navigation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.database.fakes.FakeClassroomRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeCourseRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeRestaurantRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.utils.TabAction
import com.github.sdp.ratemyepfl.utils.ViewPagerAction
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ReviewFragmentTest {
    @get:Rule
    val hiltAndroidRule: HiltAndroidRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Test
    fun loadsCourseFragmentWhenUsersPressesOnCourseTab() {
        HiltUtils.launchFragmentInHiltContainer<ReviewFragment> {}
        TabAction.selectTab(ReviewableTabFragment.TAB.COURSE.tabName)
        checkCourse()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadsClassroomFragmentWhenUsersPressesOnClassroomTab() {
        val scenario =
            HiltUtils.launchFragmentInHiltContainer<ReviewFragment> {}
        TabAction.selectTab(ReviewableTabFragment.TAB.CLASSROOM.tabName)
        checkClassroom()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadsRestaurantFragmentWhenUsersPressesOnRestaurantTab() {
        val scenario =
            HiltUtils.launchFragmentInHiltContainer<ReviewFragment> {}
        TabAction.selectTab(ReviewableTabFragment.TAB.RESTAURANT.tabName)
        checkRestaurant()
    }


    @ExperimentalCoroutinesApi
    @Test
    fun loadsClassroomFragmentWhenUsersSwipeToTheRightOfCourseTab() {
        val scenario =
            HiltUtils.launchFragmentInHiltContainer<ReviewFragment> { }
        ViewPagerAction.swipeNext()
        checkClassroom()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadsRestaurantFragmentWhenUsersSwipeToTheRightOfClassroomTab() {
        val scenario =
            HiltUtils.launchFragmentInHiltContainer<ReviewFragment> {}
        ViewPagerAction.apply {
            swipeNext()
        }.swipeNext()
        checkRestaurant()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadsClassroomFragmentWhenUsersSwipeToTheLeftOfRestaurantTab() {
        val scenario =
            HiltUtils.launchFragmentInHiltContainer<ReviewFragment> {}
        TabAction.onTab(ReviewableTabFragment.TAB.RESTAURANT.tabName)
            .perform(click())
        ViewPagerAction.swipePrevious()
        checkClassroom()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadsCourseFragmentWhenUsersSwipeToTheLeftOfClassroomTab() {
        val scenario =
            HiltUtils.launchFragmentInHiltContainer<ReviewFragment> {}
        TabAction.onTab(ReviewableTabFragment.TAB.RESTAURANT.tabName)
            .perform(click())
        ViewPagerAction.apply { swipePrevious() }.swipePrevious()
        checkCourse()
    }


    private fun checkClassroom() {
        onView(withText(FakeClassroomRepository.CLASSROOM_LIST[0].toString())).check(
            matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    private fun checkCourse() {
        onView(withText(FakeCourseRepository.COURSE_LIST[0].toString())).check(
            matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    private fun checkRestaurant() {
        onView(withText(FakeRestaurantRepository.RESTAURANT_LIST[0].toString())).check(
            matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

}