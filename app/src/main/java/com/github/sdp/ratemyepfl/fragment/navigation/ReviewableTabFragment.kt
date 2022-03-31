package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

sealed class ReviewableTabFragment(layout: Int) : Fragment(layout) {
    protected inline fun <reified T : AppCompatActivity> displayContent() {
        val intent = Intent(this.activity, T::class.java)
        startActivity(intent)
    }

    companion object {
        const val NUMBER_OF_TABS = 3
        const val COURSE_TAB_NAME = "Course"
        const val CLASSROOM_TAB_NAME = "Classroom"
        const val RESTAURANT_TAB_NAME = "Restaurant"
        /**
         * Converts a position to the corresponding tab name
         *
         * @param position: index of the tab
         *
         * @return the corresponding name, (with COURSE_TAB_NAME as default)
         */
        fun fromPositionToTabName(position: Int): String = when(position) {
            0 -> COURSE_TAB_NAME
            1 -> CLASSROOM_TAB_NAME
            2 -> RESTAURANT_TAB_NAME
            else -> COURSE_TAB_NAME
        }

        /**
         * Converts a position to the corresponding tab fragment
         *
         * @param position: the tab index of the fragment
         *
         * @return the corresponding fragment (with CourseTabFragment as default)
         */
        fun fromPositionToFragment(position: Int): Fragment = when (position) {
            0 -> CourseTabFragment()
            2 -> RestaurantTabFragment()
            1 -> ClassroomTabFragment()
            else -> CourseTabFragment()
        }
    }
}
