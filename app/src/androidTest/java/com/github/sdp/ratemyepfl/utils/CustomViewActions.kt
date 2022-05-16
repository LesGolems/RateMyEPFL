package com.github.sdp.ratemyepfl.utils

import android.view.View
import android.widget.NumberPicker
import android.widget.RatingBar
import android.widget.SearchView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.hamcrest.Matcher

object CustomViewActions {
    inline fun <reified T : View> createViewAction(crossinline perform: (UiController?, View?) -> Unit): ViewAction =
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

    object RatingAction {
        private fun performSetRating(value: Float) = object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(RatingBar::class.java)
            }

            override fun getDescription(): String {
                return "Custom view action to set rating."
            }

            override fun perform(uiController: UiController?, view: View?) {
                val ratingBar = view as RatingBar
                ratingBar.rating = value
            }

        }

        fun performSetRating(rating: ReviewRating) =
            performSetRating(rating.rating.toFloat())
    }

    /**
     * Defines the behavior to test a search bar
     */
    object SearchAction {
        /**
         * Write the [request] inside the search view
         */
        private fun type(request: String) {
            onSearchBar()
                .perform(ViewActions.typeText(request))
        }

        private fun startSearch() {
            onSearchBar()
                .perform(ViewActions.click())
        }

        private fun finishSearch() {
            Espresso.closeSoftKeyboard()
        }

        fun query(request: String) {
            startSearch()
            type(request)
            finishSearch()
        }

        private fun onSearchBar(): ViewInteraction =
            onView(ViewMatchers.isAssignableFrom(SearchView::class.java))
    }

    object NumberPickerActions {

        /**
         * Set the number of a number picker
         */
        fun setNumber(num: Int): ViewAction {
            return object : ViewAction {
                override fun perform(uiController: UiController?, view: View) {
                    val np = view as NumberPicker
                    np.value = num
                }

                override fun getDescription(): String {
                    return "Set the passed number into the NumberPicker"
                }

                override fun getConstraints(): Matcher<View> {
                    return ViewMatchers.isAssignableFrom(NumberPicker::class.java)
                }
            }
        }
    }

}