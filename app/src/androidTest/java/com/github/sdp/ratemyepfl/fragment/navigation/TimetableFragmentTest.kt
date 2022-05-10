package com.github.sdp.ratemyepfl.fragment.navigation

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.sdp.ratemyepfl.activity.HiltTestActivity
import com.github.sdp.ratemyepfl.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.database.fakes.FakeUserRepository
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.utils.TabAction
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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