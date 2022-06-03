package com.github.sdp.ratemyepfl.ui.fragment.profile

import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.ui.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.ui.fragment.main.CourseTabFragment
import com.github.sdp.ratemyepfl.viewmodel.profile.AddClassViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * List of courses but changing the onClick behaviour
 */
@AndroidEntryPoint
class SelectCourseFragment : CourseTabFragment() {
    override val reviewableAdapter = ReviewableAdapter { t -> submitCourse(t) }
    private val addClassViewModel: AddClassViewModel by activityViewModels()

    private fun submitCourse(course: Reviewable) {
        val c = course as Course
        addClassViewModel.course.postValue(c)
        Navigation.findNavController(requireView()).popBackStack()
    }

}