package com.github.sdp.ratemyepfl

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.item.FakeCoursesDatabase

class CoursesActivity : AppCompatActivity() {
    private lateinit var coursesView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)

        val fakeDatabase = FakeCoursesDatabase()
        val courseList = fakeDatabase.getCoursesList()

        coursesView = findViewById(R.id.coursesListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
            courseList)
        coursesView.adapter = adapter
    }
}