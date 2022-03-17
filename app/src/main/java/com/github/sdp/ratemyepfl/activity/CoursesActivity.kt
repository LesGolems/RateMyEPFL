package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.placeholder.CoursesRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoursesActivity : AppCompatActivity() {

    private lateinit var coursesView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)

        var courseList: List<Course?> = listOf()
        runBlocking {
            launch {
                courseList = CoursesRepository().get()
            }
        }

        coursesView = findViewById(R.id.coursesListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, courseList)
        coursesView.adapter = adapter
        coursesView.isClickable = true
        coursesView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            courseList[position]?.let { displayCourseReviews(it.name) }
        }
    }

    private fun displayCourseReviews(courseName: String) {
        val intent = Intent(this, ReviewsActivity::class.java)
        intent.putExtra(ReviewsActivity.EXTRA_COURSE_NAME, courseName)
        startActivity(intent)
    }
}