package com.github.sdp.ratemyepfl.fragment.navigation

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.github.sdp.ratemyepfl.activity.HiltTestActivity
import com.github.sdp.ratemyepfl.auth.FakeConnectedUser
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DayFragmentTest {
    lateinit var scenario: ActivityScenario<HiltTestActivity>

    @get:Rule
    val hiltAndroidRule: HiltAndroidRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        FakeConnectedUser.instance = FakeConnectedUser.Instance.FAKE_USER_1
        HiltUtils.launchFragmentInHiltContainer<DayFragment> { }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun recyclerViewDisplayed() {
        onRecyclerView().check(matches(isDisplayed()))
    }

    fun onRecyclerView(): ViewInteraction = onView(isAssignableFrom(RecyclerView::class.java))
}