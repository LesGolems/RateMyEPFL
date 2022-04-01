package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.CourseListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseTabFragment : ReviewableTabFragment(R.layout.fragment_course_tab) {

    private val viewModel: CourseListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getItemsAsLiveData().observe(viewLifecycleOwner) {
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