package com.github.sdp.ratemyepfl.fragment.review.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.CourseReviewViewModel
import com.github.sdp.ratemyepfl.viewmodel.RoomReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseReviewInfoFragment : Fragment(R.layout.fragment_course_review_info) {

    private val viewModel by activityViewModels<CourseReviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCourse().observe(viewLifecycleOwner){
            view.findViewById<TextView>(R.id.testinfo2).text = it!!.id
        }
    }
}