package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.course.CourseListActivity

class CourseTabFragment : ReviewableTabFragment(R.layout.fragment_course_tab) {
    private lateinit var courseButton: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        courseButton = view.findViewById<Button?>(R.id.courseTabButton).apply {
            setOnClickListener {
                displayContent<CourseListActivity>()
            }
        }

    }


}