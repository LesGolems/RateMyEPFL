package com.github.sdp.ratemyepfl.activity.course

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewableListActivity
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.ListActivityUtils
import com.github.sdp.ratemyepfl.utils.ListActivityUtils.Companion.setUpSearchView
import com.github.sdp.ratemyepfl.viewmodel.CourseListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class CourseListActivity : ReviewableListActivity<Course>() {

    private val viewModel: CourseListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getCourses().observe(this) {
            it?.let {
                reviewableAdapter.setData(it as MutableList<Reviewable>)
            }
        }
    }

    override fun displayReviews(t: Course) {
        val intent = Intent(this, CourseReviewListActivity::class.java)
        intent.putExtra(CourseReviewListActivity.EXTRA_COURSE_JSON, Json.encodeToString(t))
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.courses_options_menu, menu)
        setUpSearchView(menu, reviewableAdapter, R.id.courseSearchView)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.increasingOrder -> {
            reviewableAdapter.sortAlphabetically(true)
            true
        }
        R.id.decreasingOrder -> {
            reviewableAdapter.sortAlphabetically(false)
            true
        }
        R.id.credit_2, R.id.credit_3, R.id.credit_4, R.id.credit_5, R.id.credit_6, R.id.credit_7, R.id.credit_8 -> {
            reviewableAdapter.filterByCredit(item.title)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}