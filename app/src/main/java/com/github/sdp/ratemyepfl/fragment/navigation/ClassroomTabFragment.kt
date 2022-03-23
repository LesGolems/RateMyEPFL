package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.classrooms.ClassroomsListActivity

class ClassroomTabFragment : TabFragment(R.layout.fragment_classroom_tab) {
    private lateinit var button: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button = view.findViewById<Button?>(R.id.classroomTabButton).apply {
            setOnClickListener {
                displayContent()
            }
        }
    }

    override fun displayContent() {
        val intent = Intent(this.activity, ClassroomsListActivity::class.java)
        startActivity(intent)
    }
}