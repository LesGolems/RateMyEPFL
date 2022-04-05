package com.github.sdp.ratemyepfl.utils

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.google.android.material.tabs.TabLayout
import org.hamcrest.CoreMatchers

object TabAction {
    fun selectTab(tabName: String) {
        onTab(tabName).perform(ViewActions.click())
    }

    fun verifySelectedTab(text: String) {
        onTab(text).check(ViewAssertions.matches(ViewMatchers.isSelected()))
    }

    fun onTab(tabName: String) = onView(
        CoreMatchers.allOf(
            ViewMatchers.isDescendantOfA(ViewMatchers.isAssignableFrom(TabLayout::class.java)),
            ViewMatchers.withChild(withText(tabName))
        )
    )
}