package com.github.sdp.ratemyepfl.fragment.navigation

import com.github.sdp.ratemyepfl.database.fakes.FakeRestaurantRepository
import com.github.sdp.ratemyepfl.fragment.navigation.util.ReviewableTabFragmentTestContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RestaurantTabFragmentTest {
    @get:Rule
    val hiltAndroidTestRule = HiltAndroidRule(this)
}