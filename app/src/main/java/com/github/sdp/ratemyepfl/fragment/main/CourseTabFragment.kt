package com.github.sdp.ratemyepfl.fragment.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.viewmodel.filter.CourseFilter
import com.github.sdp.ratemyepfl.viewmodel.filter.ReviewableFilter
import com.github.sdp.ratemyepfl.viewmodel.main.CourseListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class CourseTabFragment : ReviewableTabFragment<Course>(R.menu.courses_options_menu) {

    override val viewModel: CourseListViewModel by viewModels()

    override val reviewActivityMenuId: Int = R.menu.bottom_navigation_menu_course_review
    override val reviewActivityGraphId: Int = R.navigation.nav_graph_course_review
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.elements
            .observe(viewLifecycleOwner) { courses ->
                reviewableAdapter.submitData(courses)
            }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean = if (isResumed) {
        when (item.contentDescription) {
            getString(R.string.credits_item) -> {
                displayResult(
                    viewModel.loadIfAbsent(
                        CourseFilter.Credits(
                            item.title.toString().toInt()
                        )
                    )
                )
                true
            }
            getString(R.string.section_item_description) -> {
                displayResult(
                    viewModel.loadIfAbsent(
                        CourseFilter.Section(
                            item.titleCondensed.toString()
                        )
                    )
                )
                true
            }
            getString(R.string.cycle_picker_description) -> {
                displayResult(
                    viewModel.loadIfAbsent(
                        CourseFilter.Cycle(
                            item.titleCondensed.toString()
                        )
                    )
                )
                true
            }
            else -> {
                super.onContextItemSelected(item)
            }
        }
    } else false

    override fun alphabeticFilter(reverse: Boolean) =
        if (!reverse) CourseFilter.AlphabeticalOrder else CourseFilter.AlphabeticalOrderReversed

    override fun bestRatedFilter(): ReviewableFilter<Course> = CourseFilter.BestRated

    override fun worstRatedFilter(): ReviewableFilter<Course> = CourseFilter.WorstRated
}