package com.github.sdp.ratemyepfl.utils

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.android.material.tabs.TabLayout
import org.hamcrest.CoreMatchers.allOf

object TabAction {
    fun selectTab(tabName: String) {
        onTab(tabName).perform(click())
    }

    fun onTab(tabName: String): ViewInteraction = onView(
        allOf(
            isDescendantOfA(isAssignableFrom(TabLayout::class.java)),
            withChild(withText(tabName))
        )
    )
}