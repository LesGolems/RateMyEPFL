package com.github.sdp.ratemyepfl.activity

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.sdp.ratemyepfl.activity.map.MapActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule

@HiltAndroidTest
class MapActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testRule = ActivityScenarioRule(MapActivity::class.java)


}