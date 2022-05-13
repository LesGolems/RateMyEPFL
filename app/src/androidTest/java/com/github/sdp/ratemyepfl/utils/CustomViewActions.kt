package com.github.sdp.ratemyepfl.utils

import android.graphics.Point
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.widget.RatingBar
import android.widget.SearchView
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
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

    fun pressesTab(tabPosition: Int) = createViewAction<TabLayout> { _, view ->
        (view as TabLayout).getTabAt(tabPosition)?.select()
    }

    fun getInRecyclerView(item: Int, retrieve: (String?) -> Unit) =
        createViewAction<RecyclerView> { _, view ->
            val selected =
                (view as RecyclerView).get(item).findViewById<TextView>(R.id.reviewableId).text
            retrieve(selected.toString())
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

    object FingerGestureActions {

        /**
         * Pinches out the view with the fingers (ZOOM IN)
         */
        fun pinchOut(startDelta: Int = 5, endDelta: Int = 500): ViewAction =
            pinch(startDelta, endDelta, "Pinch Out")

        /**
         * Pinches in the view with the fingers (ZOOM OUT)
         */
        fun pinchIn(startDelta: Int = 500, endDelta: Int = 5): ViewAction =
            pinch(startDelta, endDelta, "Pinch In")

        /**
         * Performs a finger "pinch" on a view.
         *
         * @param startDelta The distance of each finger to the center point at the beginning.
         * @param endDelta The distance of each finger to the center point at the end.
         *
         * @return a [ViewAction] performing a finger "pinch" on a [View].
         */
        private fun pinch(startDelta: Int, endDelta: Int, description: String): ViewAction {
            return object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return ViewMatchers.isEnabled()
                }

                override fun getDescription(): String {
                    return description
                }

                override fun perform(uiController: UiController, view: View) {
                    val middlePosition: Point = TestUtils.centerPoint(view)
                    val startPoint1 = Point(middlePosition.x - startDelta, middlePosition.y)
                    val startPoint2 = Point(middlePosition.x + startDelta, middlePosition.y)
                    val endPoint1 = Point(middlePosition.x - endDelta, middlePosition.y)
                    val endPoint2 = Point(middlePosition.x + endDelta, middlePosition.y)
                    performPinch(uiController, startPoint1, startPoint2, endPoint1, endPoint2)
                }
            }
        }

        /**
         * Start a stream of position events
         */
        private fun performPinch(
            uiController: UiController,
            startPoint1: Point,
            startPoint2: Point,
            endPoint1: Point,
            endPoint2: Point
        ) {
            val duration = 500
            val eventMinInterval: Long = 10
            // The time when the user originally pressed down to start a stream of position events
            val startTime = SystemClock.uptimeMillis()
            // The time when this specific event was generated
            var eventTime = startTime
            var event: MotionEvent
            // Coordinates of first finger
            var eventX1: Float = startPoint1.x.toFloat()
            var eventY1: Float = startPoint1.y.toFloat()
            // Coordinates of second finger
            var eventX2: Float = startPoint2.x.toFloat()
            var eventY2: Float = startPoint2.y.toFloat()

            // The properties of the two touch points
            val pointerProperties = arrayOfNulls<MotionEvent.PointerProperties>(2)
            val pp1 = MotionEvent.PointerProperties()
            pp1.id = 0
            pp1.toolType = MotionEvent.TOOL_TYPE_FINGER
            val pp2 = MotionEvent.PointerProperties()
            pp2.id = 1
            pp2.toolType = MotionEvent.TOOL_TYPE_FINGER
            pointerProperties[0] = pp1
            pointerProperties[1] = pp2

            // The coordinates of the two touch points
            val pointerCoords = arrayOfNulls<MotionEvent.PointerCoords>(2)
            val pc1 = MotionEvent.PointerCoords()
            pc1.x = eventX1
            pc1.y = eventY1
            pc1.pressure = 1f
            pc1.size = 1f
            val pc2 = MotionEvent.PointerCoords()
            pc2.x = eventX2
            pc2.y = eventY2
            pc2.pressure = 1f
            pc2.size = 1f
            pointerCoords[0] = pc1
            pointerCoords[1] = pc2

            // Perform the pinch
            try {
                /* 1. Send ACTION_DOWN event of one start point */
                event = MotionEvent.obtain(
                    startTime, eventTime,
                    MotionEvent.ACTION_DOWN,
                    1, pointerProperties, pointerCoords,
                    0, 0, 1f, 1f, 0, 0, 0, 0
                )
                uiController.injectMotionEvent(event)

                /* 2. Send ACTION_POINTER_DOWN of two start points*/
                event = MotionEvent.obtain(
                    startTime, eventTime,
                    MotionEvent.ACTION_POINTER_DOWN + (pp2.id shl MotionEvent.ACTION_POINTER_INDEX_SHIFT),
                    2, pointerProperties, pointerCoords,
                    0, 0, 1f, 1f, 0, 0, 0, 0
                )
                uiController.injectMotionEvent(event)

                /* 3. Send ACTION_MOVE of two middle points */
                /* 4. Repeat with updated middle points (x,y), until reach the end points */
                val moveEventNumber = (duration / eventMinInterval).toFloat()
                val stepX1: Float = (endPoint1.x - startPoint1.x) / moveEventNumber
                val stepY1: Float = (endPoint1.y - startPoint1.y) / moveEventNumber
                val stepX2: Float = (endPoint2.x - startPoint2.x) / moveEventNumber
                val stepY2: Float = (endPoint2.y - startPoint2.y) / moveEventNumber

                // Update the move events
                for (i in 0 until moveEventNumber.toInt()) {
                    eventTime += eventMinInterval
                    eventX1 += stepX1
                    eventY1 += stepY1
                    eventX2 += stepX2
                    eventY2 += stepY2
                    pc1.x = eventX1
                    pc1.y = eventY1
                    pc2.x = eventX2
                    pc2.y = eventY2
                    pointerCoords[0] = pc1
                    pointerCoords[1] = pc2
                    event = MotionEvent.obtain(
                        startTime, eventTime,
                        MotionEvent.ACTION_MOVE,
                        2, pointerProperties, pointerCoords,
                        0, 0, 1f, 1f, 0, 0, 0, 0
                    )
                    uiController.injectMotionEvent(event)
                }

                /* 5. Send ACTION_POINTER_UP of two end points */
                pc1.x = endPoint1.x.toFloat()
                pc1.y = endPoint1.y.toFloat()
                pc2.x = endPoint2.x.toFloat()
                pc2.y = endPoint2.y.toFloat()
                pointerCoords[0] = pc1
                pointerCoords[1] = pc2
                eventTime += eventMinInterval
                event = MotionEvent.obtain(
                    startTime, eventTime,
                    MotionEvent.ACTION_POINTER_UP + (pp2.id shl MotionEvent.ACTION_POINTER_INDEX_SHIFT),
                    2, pointerProperties, pointerCoords,
                    0, 0, 1f, 1f, 0, 0, 0, 0
                )
                uiController.injectMotionEvent(event)


                /* 6. Send ACTION_UP of one end point */
                eventTime += eventMinInterval
                event = MotionEvent.obtain(
                    startTime, eventTime,
                    MotionEvent.ACTION_UP,
                    1, pointerProperties, pointerCoords,
                    0, 0, 1f, 1f, 0, 0, 0, 0
                )
                uiController.injectMotionEvent(event)

            } catch (e: InjectEventSecurityException) {
                throw RuntimeException("The pinch could not be performed", e)
            }
        }
    }
}