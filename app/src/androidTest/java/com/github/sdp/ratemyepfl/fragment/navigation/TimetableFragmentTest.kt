package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.sdp.ratemyepfl.activity.HiltTestActivity
import com.github.sdp.ratemyepfl.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.database.fakes.FakeUserRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.utils.TabAction
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TimetableFragmentTest {
    lateinit var scenario: ActivityScenario<HiltTestActivity>

    @get:Rule
    val hiltAndroidRule: HiltAndroidRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        scenario = HiltUtils.launchFragmentInHiltContainer<TimetableFragment> { }
    }

    /*
    @Test
    fun addClassFilledWorks(){
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.mainActivityDrawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.timetable)).perform(click())
        onView(withId(R.id.addClassButton)).perform(click())
        onView(withId(R.id.addClassTitle)).check(matches(isDisplayed()))
        scenario.close()
    }
    
     */

    @Test
    fun navigationTest(){
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = ActivityScenario.launch(intent)
        onView(withId(R.id.mainActivityDrawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.timetable)).perform(click())
        onView(withId(R.id.addClassButton)).perform(click())
        onView(withId(R.id.addClassTitle)).check(matches(isDisplayed()))
        scenario.close()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadsDayFragment() {
        TabAction.selectTab(DayFragment.DAYS.MONDAY.day)
        checkDay(0)
    }

    private fun checkDay(day: Int) {
        onView(
            withText(
                FakeUserRepository.timetable[0].name
            )
        ).check(
            matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

}