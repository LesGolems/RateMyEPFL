package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.course.CourseListActivity

class CourseTabFragment : TabFragment(R.layout.fragment_course_tab) {
    private lateinit var courseButton: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        courseButton = view.findViewById<Button?>(R.id.courseTabButton).apply {
            setListeners(this)
        }

    }

    override fun displayContent() {
        val intent = Intent(this.activity, CourseListActivity::class.java)
        startActivity(intent)
    }

}