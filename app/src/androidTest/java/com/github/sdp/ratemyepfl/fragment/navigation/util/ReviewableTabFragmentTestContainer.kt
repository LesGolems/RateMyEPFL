package com.github.sdp.ratemyepfl.fragment.navigation.util

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.database.fakes.FakeClassroomRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeCourseRepository
import com.github.sdp.ratemyepfl.database.fakes.FakeRestaurantRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.fragment.navigation.ClassroomTabFragment
import com.github.sdp.ratemyepfl.fragment.navigation.CourseTabFragment
import com.github.sdp.ratemyepfl.fragment.navigation.RestaurantTabFragment
import com.github.sdp.ratemyepfl.fragment.navigation.ReviewableTabFragment
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.CustomViewActions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.reflect.KClass

/**
 * Container for behavior that is inherited from a [ReviewableTabFragment]
 */
class ReviewableTabFragmentTestContainer<U: Reviewable, T : ReviewableTabFragment<U>> constructor(val testedFragment: KClass<T>) {

    /**
     * Test all inherited behavior
     */
    @ExperimentalCoroutinesApi
    fun testInheritedBehavior() {
        isSearchDisplayed()
        getList()?.let {
            searchFor(it[it.size - 1].toString())
        }
        nonEmptySuccessfulQuery()
        emptyQuery()
        filterAlphabetically(false)
        filterAlphabetically(true)
        startReviewOnClick()
    }

    @ExperimentalCoroutinesApi
    fun isSearchDisplayed() {
        createScenario()
        onRecyclerView().check(matches(isDisplayed()))
    }

    @ExperimentalCoroutinesApi
    fun searchFor(item: String) {
        createScenario()
        CustomViewActions.SearchAction.query(item)
        onRecyclerView().check(matches(hasDescendant(withText(item))))
    }

    @ExperimentalCoroutinesApi
    fun nonEmptySuccessfulQuery() {
        createScenario()
        getList()?.size?.run {
            val item = getItem(max(this - 1, 0))
            CustomViewActions.SearchAction.query(item.toString() + "\n")
            onRecyclerView().check(
                matches(hasDescendant(withText(item.toString())))
            )
        }
    }

    @ExperimentalCoroutinesApi
    fun emptyQuery() {
        createScenario()
        getList()?.size?.run {
            CustomViewActions.SearchAction.query("")
            for (i in 0..min(getList()?.size ?: 0, 2)) {
                onRecyclerView().check(
                    matches(hasDescendant(withText(getItem(i).toString())))
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun filterAlphabetically(reverseOrder: Boolean) {
        createScenario()
        onView(withId(R.id.filterMenuButton))
            .perform(click())
            .run {
                if (reverseOrder) {
                    this.perform(click())
                }
            }
        val text: String? = getList()?.map { it.toString() }
            ?.sortedBy {
                it
            }?.let {
                if (reverseOrder) {
                    it.reversed()
                } else it
            }?.first().toString()
        onRecyclerView().check(matches(hasDescendant(withText(text))))
    }

    fun onRecyclerView(): ViewInteraction = onView(isAssignableFrom(RecyclerView::class.java))

    @ExperimentalCoroutinesApi
    fun startReviewOnClick() {
        createScenario()
        init()
        Espresso.onView(withText(getItem(0)?.toString()))
            .perform(click())
        intended(toPackage("com.github.sdp.ratemyepfl"))
        release()
    }

    @ExperimentalCoroutinesApi
    private fun createScenario() = when (testedFragment) {
        ClassroomTabFragment::class -> HiltUtils.launchFragmentInHiltContainer<ClassroomTabFragment> { }
        CourseTabFragment::class -> HiltUtils.launchFragmentInHiltContainer<CourseTabFragment> { }
        RestaurantTabFragment::class -> HiltUtils.launchFragmentInHiltContainer<RestaurantTabFragment> { }
        else -> {}
    }

    private fun getItem(position: Int): Reviewable? = getList()?.get(position)

    private fun getList(): List<Reviewable>? = when (testedFragment) {
        ClassroomTabFragment::class -> FakeClassroomRepository.CLASSROOM_LIST
        CourseTabFragment::class -> FakeCourseRepository.COURSE_LIST
        RestaurantTabFragment::class -> FakeRestaurantRepository.RESTAURANT_LIST
        else -> null
    }
}