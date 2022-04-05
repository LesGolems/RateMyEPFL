package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.CourseListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseTabFragment : ReviewableTabFragment() {

    private val viewModel: CourseListViewModel by viewModels()

    override val reviewActivityLayoutId: Int = R.layout.activity_course_review
    override val filterMenuId: Int = R.menu.courses_options_menu

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.courses
            .observe(viewLifecycleOwner) { courses ->
                reviewableAdapter.submitData(courses)
            }
    }

//    override fun onContextItemSelected(item: MenuItem): Boolean = when (item.itemId) {
//        R.id.credit_2, R.id.credit_3, R.id.credit_4, R.id.credit_5, R.id.credit_6, R.id.credit_7, R.id.credit_8 -> {
//            reviewableAdapter.filterByCredit(item.title)
//            true
//        }
//        else -> {
//            super.onContextItemSelected(item)
//        }
//    }

    override fun onResume() {
        // BUGFIX
        viewModel.courses.postValue(viewModel.courses.value ?: listOf())
        super.onResume()
    }
}