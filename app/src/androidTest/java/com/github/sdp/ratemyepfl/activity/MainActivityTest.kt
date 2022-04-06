package com.github.sdp.ratemyepfl.activity

import android.Manifest
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.utils.CustomViewActions.navigateTo
import com.github.sdp.ratemyepfl.utils.TestUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

    @Before
    fun setup() {

    }

    //    /**
//     * Template test to a BottomNavigationView
//     * The old style with supportFragmentManager does not work since we do not use the
//     * manager anymore
//     */
    @Test
    fun navigateHomePageAddsTheCorrectFragment() {
        navigate(Destination.REVIEW)
        navigate(Destination.HOME)
        checkHomePage()
    }

    //
    @Test
    fun pressesBackReturnsToHome() {
        navigate(Destination.REVIEW)
        navigate(Destination.EVENT)
        navigate(Destination.MAP)
        Espresso.pressBack()
        checkHomePage()
    }

    private fun checkHomePage() = checkPage(Destination.HOME)
    private fun checkReviewPage() = checkPage(Destination.REVIEW)
    private fun checkEventPage() = checkPage(Destination.EVENT)
    private fun checkMapPage() = checkPage(Destination.MAP)

    private fun checkPage(destination: Destination) =
        onView(first(withText(destination.getString()))).check(
            matches(isDisplayed())
        )

    private fun navigate(destination: Destination): ViewInteraction =
        onView(withId(R.id.activityMainBottomNavigationView)).perform(navigateTo(destination.id))

    enum class Destination(val id: Int, val stringId: Int) {
        HOME(R.id.home, R.string.home_nav_title),
        REVIEW(R.id.review, R.string.reviews_nav_title),
        EVENT(R.id.event, R.string.event_page_title),
        MAP(R.id.map, R.string.map_nav_title);

        fun getString() = TestUtils.getString(stringId)
    }


    //
    @Test
    fun navigateToReviewAddsTheCorrectFragment() {
        navigate(Destination.REVIEW)
        checkReviewPage()
    }

    @Test
    fun navigateToEventPageAddsTheCorrectFragment() {
        navigate(Destination.EVENT)
        checkEventPage()
    }

    @Test
    fun navigateToMapPageAddsTheCorrectFragment() {
        navigate(Destination.MAP)
        checkMapPage()
    }

    private fun <T> first(matcher: Matcher<T>): Matcher<T>? {
        return object : BaseMatcher<T>() {
            var isFirst = true
            override fun matches(item: Any): Boolean {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false
                    return true
                }
                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("should return first matching item")
            }
        }
    }

}