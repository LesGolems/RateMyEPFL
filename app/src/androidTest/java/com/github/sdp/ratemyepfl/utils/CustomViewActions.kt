package com.github.sdp.ratemyepfl.utils

import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.github.sdp.ratemyepfl.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Matcher

object CustomViewActions {
    private inline fun <reified T : View> createViewAction(crossinline perform: (UiController?, View?) -> Unit): ViewAction =
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

    fun getInRecyclerView(item: Int, retrieve: (String?) -> Unit) =
        createViewAction<RecyclerView> { _, view ->
            val selected =
                (view as RecyclerView).get(0).findViewById<TextView>(R.id.reviewableId).text
            retrieve(selected.toString())
        }

    /**
     * Defines the behavior to test a search bar
     */
    object SearchAction {
        /**
         * Write the [request] inside the search view
         */
        fun type(request: String) {
            onSearchBar()
                .perform(ViewActions.typeText(request))
        }

        fun startSearch() {
            onSearchBar()
                .perform(ViewActions.click())
        }

        fun finishSearch() {
            Espresso.closeSoftKeyboard()
        }

        fun query(request: String) {
            startSearch()
            type(request)
            finishSearch()
        }

        fun onSearchBar(): ViewInteraction =
            onView(ViewMatchers.isAssignableFrom(SearchView::class.java))
    }
}