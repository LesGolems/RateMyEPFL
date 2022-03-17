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
import com.github.sdp.ratemyepfl.placeholder.CoursesRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.github.sdp.ratemyepfl.viewmodel.CourseListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseListActivity : AppCompatActivity() {

    private lateinit var coursesView: ListView
    val viewModel by viewModels<CourseListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)

        coursesView = findViewById(R.id.coursesListView)
        coursesView.isClickable = true

        viewModel.getCourses().observe(this){
            it?.let{ it ->
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, it)
                coursesView.adapter = adapter
                coursesView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                    it[position]?.let { it1 -> displayCourseReviews(it1.name) }
                }
                }
            }
        }


    private fun displayCourseReviews(courseName: String) {
        val intent = Intent(this, CourseReviewListActivity::class.java)
        intent.putExtra(CourseReviewListActivity.EXTRA_COURSE_NAME, courseName)
        startActivity(intent)
    }
}