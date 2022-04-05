package com.github.sdp.ratemyepfl.fragment.navigation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.database.FakeClassroomRepository
import com.github.sdp.ratemyepfl.database.FakeCourseRepository
import com.github.sdp.ratemyepfl.database.FakeRestaurantRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
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
        onView(withText(ReviewableTabFragment.TAB.COURSE.name)).perform(click())
        checkCourse()
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
    fun loadsRestaurantFragmentWhenUsersPressesOnRestaurantTab() {
        val scenario =
            HiltUtils.launchFragmentInHiltContainer<ReviewFragment> {}
        onView(withText(ReviewableTabFragment.TAB.RESTAURANT.name)).perform(click())
        checkRestaurant()
    }


    @ExperimentalCoroutinesApi
    fun loadsClassroomFragmentWhenUsersSwipeToTheRightClassroomTab() {
        TODO("Implement a swipe")
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