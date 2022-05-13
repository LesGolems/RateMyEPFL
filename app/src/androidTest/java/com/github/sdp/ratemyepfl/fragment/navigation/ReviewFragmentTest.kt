package com.github.sdp.ratemyepfl.fragment.navigation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.database.fakes.FakeClassroomRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeCourseRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeRestaurantRepository
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepositoryImpl
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.utils.TabAction
import com.github.sdp.ratemyepfl.utils.ViewPagerAction
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ReviewFragmentTest {
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
    fun setup() = runTest{
        hiltAndroidRule.inject()
        courseRepo.add(FakeCourseRepository.COURSE_LIST[0]).await()
        classroomRepo.add(FakeClassroomRepository.CLASSROOM_LIST[0]).await()
        restaurantRepo.add(FakeRestaurantRepository.RESTAURANT_LIST[0]).await()
    }

    @After
    fun teardown() = runTest {
        courseRepo.remove(FakeCourseRepository.COURSE_LIST[0].getId()).await()
        classroomRepo.remove(FakeClassroomRepository.CLASSROOM_LIST[0].getId()).await()
        restaurantRepo.remove(FakeRestaurantRepository.RESTAURANT_LIST[0].getId()).await()

    }

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
        HiltUtils.launchFragmentInHiltContainer<ReviewFragment> {}
        TabAction.selectTab(ReviewableTabFragment.TAB.CLASSROOM.tabName)
        checkClassroom()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadsRestaurantFragmentWhenUsersPressesOnRestaurantTab() {
        HiltUtils.launchFragmentInHiltContainer<ReviewFragment> {}
        TabAction.selectTab(ReviewableTabFragment.TAB.RESTAURANT.tabName)
        checkRestaurant()
    }


    @ExperimentalCoroutinesApi
    @Test
    fun loadsClassroomFragmentWhenUsersSwipeToTheRightOfCourseTab() {
        HiltUtils.launchFragmentInHiltContainer<ReviewFragment> { }
        ViewPagerAction.swipeNext()
        checkClassroom()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadsRestaurantFragmentWhenUsersSwipeToTheRightOfClassroomTab() {
        HiltUtils.launchFragmentInHiltContainer<ReviewFragment> {}
        ViewPagerAction.apply {
            swipeNext()
        }.swipeNext()
        checkRestaurant()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadsClassroomFragmentWhenUsersSwipeToTheLeftOfRestaurantTab() {
        HiltUtils.launchFragmentInHiltContainer<ReviewFragment> {}
        TabAction.onTab(ReviewableTabFragment.TAB.RESTAURANT.tabName)
            .perform(click())
        ViewPagerAction.swipePrevious()
        checkClassroom()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadsCourseFragmentWhenUsersSwipeToTheLeftOfClassroomTab() {
        HiltUtils.launchFragmentInHiltContainer<ReviewFragment> {}
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