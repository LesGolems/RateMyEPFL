package com.github.sdp.ratemyepfl.utils

import android.view.View
import androidx.compose.material.TabPosition
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Matcher

object CustomViewActions {
    private inline fun<reified T: View> createViewAction(crossinline perform: (UiController?, View?) -> Unit): ViewAction =
        object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(T::class.java)
            }

            override fun getDescription(): String {
                return "Custom view action to press navigation view"
            }

            override fun perform(uiController: UiController?, view: android.view.View?) {
                perform(uiController, view)
            }
        }

    fun navigateTo(itemId: Int): ViewAction = createViewAction<BottomNavigationView> { _, view ->
        (view as BottomNavigationView).selectedItemId = itemId
    }

    fun pressesTab(tabPosition: Int) = createViewAction<TabLayout> { _, view ->
        (view as TabLayout).getTabAt(tabPosition)?.select()

    }


}