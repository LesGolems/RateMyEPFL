package com.github.sdp.ratemyepfl.activity.course

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.CoursesAdapter
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.viewmodel.CourseListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class CourseListActivity : AppCompatActivity() {

    private lateinit var coursesAdapter: CoursesAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewModel: CourseListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_list)

        coursesAdapter = CoursesAdapter { course -> displayCourseReviews(course) }
        recyclerView = findViewById(R.id.courses_recycler_view)
        recyclerView.adapter = coursesAdapter

        viewModel.getCourses().observe(this) {
            it?.let {
                coursesAdapter.setData(it as MutableList<Course>)
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

        val searchItem = menu!!.findItem(R.id.courseSearchView)
        val searchView = searchItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                coursesAdapter.filter.filter(newText)
                return true
            }
        })

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