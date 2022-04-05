package com.github.sdp.ratemyepfl.utils

import android.view.View
import androidx.core.view.ViewCompat
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.viewpager2.widget.ViewPager2
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher

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
                    ViewMatchers.isAssignableFrom(ViewPager2::class.java),
                    ViewMatchers.isDescendantOfA(ViewMatchers.isAssignableFrom(ViewPager2::class.java))
                ),
                ViewMatchers.isDisplayingAtLeast(90)
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
            val swipeAction: ViewAction
            if (vp.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                swipeAction =
                    if (isForward == vp.isRtl()) ViewActions.swipeRight() else ViewActions.swipeLeft()
            } else {
                swipeAction = if (isForward) ViewActions.swipeUp() else ViewActions.swipeDown()
            }
            swipeAction.perform(uiController, view)
        }

        private fun ViewPager2.isRtl(): Boolean {
            return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
        }
    }

    fun onViewPager() = onView(isAssignableFrom(ViewPager2::class.java))

    fun swipeNext() = onViewPager()
        .perform(SwipeAction(SwipeAction.Direction.FORWARD))

    fun swipePrevious() = onViewPager()
        .perform(SwipeAction(SwipeAction.Direction.BACKWARD))

}