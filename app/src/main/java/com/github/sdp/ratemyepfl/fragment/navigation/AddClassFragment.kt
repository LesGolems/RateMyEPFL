package com.github.sdp.ratemyepfl.fragment.navigation

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.model.items.Class
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddClassFragment : CourseTabFragment() {

    private val userProfileViewModel: UserProfileViewModel by viewModels()
    override val reviewableAdapter = ReviewableAdapter { t -> submitClass(t) }

    private fun submitClass(course: Reviewable) {
        val c = course as Course
        userProfileViewModel.addClass(Class(name = c.title, day = 0, start = 20, end = 21))
        Toast.makeText(context, "Added class " + c.title, Toast.LENGTH_SHORT).show()
        Navigation.findNavController(requireView()).popBackStack()
        // refresh timetable
        // fix navigation
    }

}