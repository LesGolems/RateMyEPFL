package com.github.sdp.ratemyepfl.activity.course

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.AddReviewActivity
import com.github.sdp.ratemyepfl.activity.ReviewableListActivity
import com.github.sdp.ratemyepfl.activity.ReviewsListActivity
import com.github.sdp.ratemyepfl.activity.classrooms.RoomReviewActivity
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.CourseListViewModel
import dagger.hilt.android.AndroidEntryPoint

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.credit_2, R.id.credit_3, R.id.credit_4, R.id.credit_5, R.id.credit_6, R.id.credit_7, R.id.credit_8 -> {
            reviewableAdapter.filterByCredit(item.title)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun displayReviews(course: Course) {
        val intent = Intent(this, CourseReviewActivity::class.java)
        intent.putExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, course.id)
        startActivity(intent)
    }

    override fun getMenuString(): Int {
        return R.menu.courses_options_menu
    }

    override fun getSearchViewString(): Int {
        return R.id.courseSearchView
    }
}