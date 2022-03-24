package com.github.sdp.ratemyepfl.fragment.navigation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MapFragmentTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Test
    fun firesAnIntentWhenUserClicksOnMapButton() {
        HiltUtils.launchFragmentInHiltContainer<MapFragment> {}

        init()
        Espresso.onView(ViewMatchers.withId(R.id.mapTabButton))
            .perform(ViewActions.click())
        intended(IntentMatchers.toPackage("com.github.sdp.ratemyepfl"))
        release()
    }
}