package com.github.sdp.ratemyepfl.activity.course

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.ListActivityUtils
import com.github.sdp.ratemyepfl.viewmodel.CourseListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class CourseListActivity : AppCompatActivity() {

    private lateinit var coursesAdapter: ReviewableAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewModel: CourseListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviewable_list)

        coursesAdapter = ReviewableAdapter { course -> displayCourseReviews(course as Course) }
        recyclerView = findViewById(R.id.reviewableRecyclerView)
        recyclerView.adapter = coursesAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        )

        viewModel.getCourses().observe(this) {
            it?.let {
                coursesAdapter.setData(it as MutableList<Reviewable>)
            }
        }

    }

    private fun displayCourseReviews(course: Course) {
        val intent = Intent(this, CourseReviewListActivity::class.java)
        intent.putExtra(CourseReviewListActivity.EXTRA_COURSE_JSON, Json.encodeToString(course))
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.courses_options_menu, menu)
        ListActivityUtils.setUpSearchView(menu, coursesAdapter, R.id.courseSearchView)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.increasingOrder -> {
            coursesAdapter.sortAlphabetically(true)
            true
        }
        R.id.decreasingOrder -> {
            coursesAdapter.sortAlphabetically(false)
            true
        }
        R.id.credit_2, R.id.credit_3, R.id.credit_4, R.id.credit_5, R.id.credit_6, R.id.credit_7, R.id.credit_8 -> {
            coursesAdapter.filterByCredit(item.title)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}