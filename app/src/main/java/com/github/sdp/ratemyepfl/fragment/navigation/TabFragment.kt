package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.sdp.ratemyepfl.activity.course.CourseListActivity

sealed class TabFragment(layout: Int): Fragment(layout) {
    protected inline fun<reified T: AppCompatActivity> displayContent() {
        val intent = Intent(this.activity, T::class.java)
        startActivity(intent)
    }
}
