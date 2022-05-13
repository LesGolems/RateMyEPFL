package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.model.items.Class
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.CourseListViewModel
import com.github.sdp.ratemyepfl.viewmodel.UserProfileViewModel
import com.github.sdp.ratemyepfl.viewmodel.filter.CourseFilter
import com.github.sdp.ratemyepfl.viewmodel.filter.ReviewableFilter
import dagger.hilt.android.AndroidEntryPoint

@Deprecated("To change")
@AndroidEntryPoint
class AddClassFragment() : ReviewableTabFragment<Course>(R.menu.courses_options_menu) {

    override val viewModel: CourseListViewModel by viewModels()
    private val userProfileViewModel: UserProfileViewModel by viewModels()
    override val reviewableAdapter = ReviewableAdapter { t -> submitClass(t) }

    override val reviewActivityMenuId: Int = R.menu.bottom_navigation_menu_course_review
    override val reviewActivityGraphId: Int = R.navigation.nav_graph_course_review

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.elements
            .observe(viewLifecycleOwner) { courses ->
                reviewableAdapter.submitData(courses)
            }
    }

    private fun submitClass(course: Reviewable) {
        val c = course as Course
        userProfileViewModel.addClass(Class(name = c.title, day = 0, start = 20, end = 21))
        Toast.makeText(context, "Added class " + c.title, Toast.LENGTH_SHORT).show()
        Navigation.findNavController(requireView()).navigate(R.id.timetableFragment)
        // refresh timetable
        // fix navigation
    }

    override fun alphabeticFilter(reverse: Boolean): ReviewableFilter<Course> =
        CourseFilter.AlphabeticalOrder

    override fun bestRatedFilter(): ReviewableFilter<Course> =
        CourseFilter.BestRated

    override fun worstRatedFilter(): ReviewableFilter<Course> =
        CourseFilter.WorstRated


}