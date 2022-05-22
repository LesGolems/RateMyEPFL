package com.github.sdp.ratemyepfl.fragment.navigation

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.AddClassViewModel
import dagger.hilt.android.AndroidEntryPoint

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