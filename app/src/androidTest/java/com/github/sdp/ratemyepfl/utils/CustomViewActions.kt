package com.github.sdp.ratemyepfl.utils

import android.view.View
import android.widget.NumberPicker
import android.widget.RatingBar
import android.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.viewpager2.widget.ViewPager2
import com.github.sdp.ratemyepfl.model.post.ReviewRating
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

object CustomViewActions {
    inline fun <reified T : View> createViewAction(crossinline perform: (UiController?, View?) -> Unit): ViewAction =
        object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(T::class.java)
            }

            override fun getDescription(): String {
                return "Custom view action to press navigation view"
            }

            override fun perform(uiController: UiController?, view: View?) {
                perform(uiController, view)
            }
        }

    fun navigateTo(itemId: Int): ViewAction = createViewAction<BottomNavigationView> { _, view ->
        (view as BottomNavigationView).selectedItemId = itemId
    }

    object RatingAction {
        private fun performSetRating(value: Float) = object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(RatingBar::class.java)
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
            onView(isAssignableFrom(SearchView::class.java))
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
                    return isAssignableFrom(NumberPicker::class.java)
                }
            }
        }
    }

    object TabAction {
        fun selectTab(tabName: String) {
            onTab(tabName).perform(ViewActions.click())
        }

        fun verifySelectedTab(text: String) {
            onTab(text).check(matches(isSelected()))
        }

        fun onTab(tabName: String): ViewInteraction = onView(
            CoreMatchers.allOf(
                isDescendantOfA(isAssignableFrom(TabLayout::class.java)),
                withChild(withText(tabName))
            )
        )

        fun selectTabAtPosition(tabIndex: Int): ViewAction {
            return object : ViewAction {
                override fun getDescription() = "with tab at index $tabIndex"

                override fun getConstraints() =
                    allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

                override fun perform(uiController: UiController, view: View) {
                    val tabLayout = view as TabLayout
                    val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                        ?: throw PerformException.Builder()
                            .withCause(Throwable("No tab at index $tabIndex"))
                            .build()

                    tabAtIndex.select()
                }
            }
        }
    }

    object ViewPagerAction {
        private class SwipeAction(val direction: Direction) : ViewAction {
            enum class Direction {
                FORWARD,
                BACKWARD
            }

            override fun getDescription(): String = "Swiping $direction"

            override fun getConstraints(): Matcher<View> =
                CoreMatchers.allOf(
                    CoreMatchers.anyOf(
                        isAssignableFrom(ViewPager2::class.java),
                        isDescendantOfA(isAssignableFrom(ViewPager2::class.java))
                    ),
                    isDisplayingAtLeast(90)
                )

            override fun perform(uiController: UiController, view: View) {
                val vp = if (view is ViewPager2) {
                    view
                } else {
                    var parent = view.parent
                    while (parent !is ViewPager2 && parent != null) {
                        parent = parent.parent
                    }
                    parent as ViewPager2
                }
                val isForward = direction == Direction.FORWARD
                val swipeAction: ViewAction =
                    if (vp.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                        if (isForward == vp.isRtl()) ViewActions.swipeRight() else ViewActions.swipeLeft()
                    } else {
                        if (isForward) ViewActions.swipeUp() else ViewActions.swipeDown()
                    }
                swipeAction.perform(uiController, view)
            }

            private fun ViewPager2.isRtl(): Boolean {
                return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
            }
        }

        private fun onViewPager(): ViewInteraction =
            onView(isAssignableFrom(ViewPager2::class.java))

        fun swipeNext(): ViewInteraction = onViewPager()
            .perform(SwipeAction(SwipeAction.Direction.FORWARD))

        fun swipePrevious(): ViewInteraction = onViewPager()
            .perform(SwipeAction(SwipeAction.Direction.BACKWARD))

    }

}