package com.github.sdp.ratemyepfl.ui.fragment.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeClassroomRepository
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeCourseRepository
import com.github.sdp.ratemyepfl.backend.database.fakes.FakeRestaurantRepository
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.ClassroomRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.utils.CustomViewActions.TabAction
import com.github.sdp.ratemyepfl.utils.CustomViewActions.ViewPagerAction
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ReviewableFragmentTest {
    @get:Rule
    val hiltAndroidRule: HiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var courseRepo: CourseRepositoryImpl

    @Inject
    lateinit var classroomRepo: ClassroomRepositoryImpl

    @Inject
    lateinit var restaurantRepo: RestaurantRepositoryImpl

    @Before
    fun setup() = runTest {
        hiltAndroidRule.inject()
        courseRepo.add(FakeCourseRepository.COURSE_LIST[0]).collect()
        classroomRepo.add(FakeClassroomRepository.CLASSROOM_LIST[0]).collect()
        restaurantRepo.add(FakeRestaurantRepository.RESTAURANT_LIST[0]).collect()
    }

    @After
    fun teardown() = runTest {
        courseRepo.remove(FakeCourseRepository.COURSE_LIST[0].getId()).collect()
        classroomRepo.remove(FakeClassroomRepository.CLASSROOM_LIST[0].getId()).collect()
        restaurantRepo.remove(FakeRestaurantRepository.RESTAURANT_LIST[0].getId()).collect()

    }

    @Test
    fun loadsCourseFragmentWhenUsersPressesOnCourseTab() {
        HiltUtils.launchFragmentInHiltContainer<ReviewableFragment> {}
        TabAction.selectTab(ReviewableTabFragment.TAB.COURSE.tabName)
        checkCourse()
    }

    @Test
    fun loadsClassroomFragmentWhenUsersPressesOnClassroomTab() {
        HiltUtils.launchFragmentInHiltContainer<ReviewableFragment> {}
        TabAction.selectTab(ReviewableTabFragment.TAB.CLASSROOM.tabName)
        checkClassroom()
    }

    @Test
    fun loadsRestaurantFragmentWhenUsersPressesOnRestaurantTab() {
        HiltUtils.launchFragmentInHiltContainer<ReviewableFragment> {}
        TabAction.selectTab(ReviewableTabFragment.TAB.RESTAURANT.tabName)
        checkRestaurant()
    }


    @Test
    fun loadsClassroomFragmentWhenUsersSwipeToTheRightOfCourseTab() {
        HiltUtils.launchFragmentInHiltContainer<ReviewableFragment> { }
        ViewPagerAction.swipeNext()
        checkClassroom()
    }

    @Test
    fun loadsRestaurantFragmentWhenUsersSwipeToTheRightOfClassroomTab() {
        HiltUtils.launchFragmentInHiltContainer<ReviewableFragment> {}
        ViewPagerAction.apply {
            swipeNext()
        }.swipeNext()
        checkRestaurant()
    }

    @Test
    fun loadsClassroomFragmentWhenUsersSwipeToTheLeftOfRestaurantTab() {
        HiltUtils.launchFragmentInHiltContainer<ReviewableFragment> {}
        TabAction.onTab(ReviewableTabFragment.TAB.RESTAURANT.tabName)
            .perform(click())
        Thread.sleep(1000)
        ViewPagerAction.swipePrevious()
        checkClassroom()
    }

    @Test
    fun loadsCourseFragmentWhenUsersSwipeToTheLeftOfClassroomTab() {
        HiltUtils.launchFragmentInHiltContainer<ReviewableFragment> {}
        TabAction.onTab(ReviewableTabFragment.TAB.RESTAURANT.tabName)
            .perform(click())
        ViewPagerAction.apply { swipePrevious() }.swipePrevious()
        checkCourse()
    }


    private fun checkClassroom() {
        Thread.sleep(1000)
        onView(withText(FakeClassroomRepository.CLASSROOM_LIST[0].toString())).check(
            matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    private fun checkCourse() {
        Thread.sleep(1000)
        onView(withText(FakeCourseRepository.COURSE_LIST[0].toString())).check(
            matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    private fun checkRestaurant() {
        Thread.sleep(1000)
        onView(withText(FakeRestaurantRepository.RESTAURANT_LIST[0].toString())).check(
            matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

}