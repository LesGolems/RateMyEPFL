package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.CourseListViewModel
import com.github.sdp.ratemyepfl.viewmodel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddClassFragment() : ReviewableTabFragment() {

    private val viewModel: CourseListViewModel by viewModels()
    private val userProfileViewModel: UserProfileViewModel by viewModels()
    override val reviewableAdapter = ReviewableAdapter { t -> submitClass(t) }

    override val reviewActivityMenuId: Int = R.menu.bottom_navigation_menu_course_review
    override val reviewActivityGraphId: Int = R.navigation.nav_graph_course_review
    override val filterMenuId: Int = R.menu.courses_options_menu

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.courses
            .observe(viewLifecycleOwner) { courses ->
                reviewableAdapter.submitData(courses)
            }
    }

    private fun submitClass(course: Reviewable) {
        //convert course into class
        Toast.makeText(context, "WORKING", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        // BUGFIX
        viewModel.courses.value?.run {
            viewModel.courses.postValue(this)
        }
        super.onResume()
    }

}