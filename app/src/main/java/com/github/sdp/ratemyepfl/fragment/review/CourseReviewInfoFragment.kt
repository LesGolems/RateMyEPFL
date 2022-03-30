package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
Fragment displayed all relevant information for a Course
 */
@AndroidEntryPoint
class CourseReviewInfoFragment : Fragment(R.layout.fragment_course_review_info) {

    // Gets the shared view model
    private val viewModel by activityViewModels<ReviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getReviewable().observe(viewLifecycleOwner) { reviewable ->
            view.findViewById<TextView>(R.id.id_course_info).text = reviewable?.toString()
        }
    }
}