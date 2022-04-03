package com.github.sdp.ratemyepfl.fragment.navigation

import com.github.sdp.ratemyepfl.dependencyinjection.HiltUtils
import com.github.sdp.ratemyepfl.model.review.Review
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ClassroomTabFragmentTest {

    @get:Rule
    val hiltAndroidTestRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @Test
    fun test() {
        HiltUtils.launchFragmentInHiltContainer<ReviewFragment> {
        }

        Thread.sleep(20000)
    }
}