package com.github.sdp.ratemyepfl.activity.course

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewableListActivity
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.viewmodel.CourseListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseListActivity : ReviewableListActivity<Course>() {

    private val viewModel: CourseListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getItemsAsLiveData().observe(this) {
            it?.let {
                reviewableAdapter.setData(it.toMutableList())
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

    override fun getMenuString(): Int {
        return R.menu.courses_options_menu
    }

    override fun getSearchViewString(): Int {
        return R.id.courseSearchView
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_course_review
    }
}