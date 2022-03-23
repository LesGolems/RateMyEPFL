package com.github.sdp.ratemyepfl.utils

import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.appcompat.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher

/**
 * Matches a hidden menu sub-item
 */
fun withMenuItemText(@StringRes menuText: Int): Matcher<View?>? {
    val matcher = withText(menuText)
    onView(matcher).check(matches(isDisplayed()))
    return matcher
}

/**
 * Matches a hidden menu item
 */
fun withOverflowMenuItemText(@StringRes menuText: Int): Matcher<View?>? {
    openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
    return withMenuItemText(menuText)
}

/**
 * Clicks on the search icon
 */
fun openSearchView(@StringRes searchViewId: Int) {
    onView(withId(searchViewId))
        .perform(click())
}

/**
 * Writes a query on the view that is opened by clicking on the search view
 */
fun typeQuery(@NonNull query: String) {
    onView(withId(R.id.search_src_text)).perform(
        typeText(query),
        closeSoftKeyboard()
    )
}

/**
 * Clicks on the cross icon
 */
fun closeSearchView() {
    onView(withId(R.id.search_close_btn))
        .perform(click())
}