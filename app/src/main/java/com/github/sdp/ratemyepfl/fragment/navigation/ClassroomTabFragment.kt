package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.classrooms.ClassroomsListActivity
import com.github.sdp.ratemyepfl.activity.course.CourseListActivity

class ClassroomTabFragment : Fragment(R.layout.fragment_classroom_tab) {
    private lateinit var button: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button = view.findViewById(R.id.classroomTabButton)
        button.setOnClickListener {
            displayClassrooms()
        }

    }

    private fun displayClassrooms() {
        val intent = Intent(this.activity, ClassroomsListActivity::class.java)
        startActivity(intent)
    }
}