package com.github.sdp.ratemyepfl.activity.course

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.viewmodel.CourseListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class CourseListActivity : AppCompatActivity() {

    private lateinit var coursesView: ListView
    val viewModel by viewModels<CourseListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_list)

        val courseList = viewModel.getCourses()

        coursesView = findViewById(R.id.coursesListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
            courseList)
        coursesView.adapter = adapter
        coursesView.isClickable = true
        coursesView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            displayCourseReviews(courseList[position])
        }
    }

    private fun displayCourseReviews(course: Course) {
        val intent = Intent(this, CourseReviewListActivity::class.java)
        intent.putExtra(CourseReviewListActivity.EXTRA_COURSE_JSON, Json.encodeToString(course))
        startActivity(intent)
    }
}