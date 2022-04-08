package com.github.sdp.ratemyepfl.fragment.navigation

import com.github.sdp.ratemyepfl.database.fakes.FakeCourseRepository
import com.github.sdp.ratemyepfl.fragment.navigation.util.ReviewableTabFragmentTestContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CourseTabFragmentTest {
    val container = ReviewableTabFragmentTestContainer(CourseTabFragment::class)

    @get:Rule
    val hiltAndroidTestRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Test
    fun startsReviewWhenUserClicksOnClassroom() {
        container.startReviewOnClick()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun isSearchDisplayed() {
        container.isSearchDisplayed()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun searchingForTheLastItemDisplaysIt() {
        container.searchFor(FakeCourseRepository.COURSE_LIST.last().toString())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun successfulQueryDisplaysTheResult() {
        container.nonEmptySuccessfulQuery()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun emptyQueryDoesNotUpdateTheList() {
        container.emptyQuery()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun filterAlphabeticallyDisplaysTheFirstItem() {
        container.filterAlphabetically(false)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun filterAlphabeticallyDisplaysTheLastItem() {
        container.filterAlphabetically(true)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun filterAlphabeticallyInReverseOrderDisplaysTheLastItem() {
        container.filterAlphabetically(true)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkInheritedBehavior() {
        container.testInheritedBehavior()
    }
}