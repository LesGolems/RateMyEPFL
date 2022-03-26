package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseReviewInfoFragment : Fragment(R.layout.fragment_course_review_info) {

    private val viewModel by activityViewModels<ReviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getReviewable().observe(viewLifecycleOwner){
            view.findViewById<TextView>(R.id.id_course_info).text = it!!.id
        }
    }
}