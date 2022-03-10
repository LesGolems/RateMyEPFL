package com.github.sdp.ratemyepfl

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.items.FakeCoursesDatabase

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
        coursesView.isClickable = true
        coursesView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            displayCourseReviews(courseList[position].name)
        }
    }

    private fun displayCourseReviews(courseName: String) {
        val intent = Intent(this, ReviewsActivity::class.java)
        intent.putExtra(ReviewsActivity.EXTRA_COURSE_NAME, courseName)
        startActivity(intent)
    }
}