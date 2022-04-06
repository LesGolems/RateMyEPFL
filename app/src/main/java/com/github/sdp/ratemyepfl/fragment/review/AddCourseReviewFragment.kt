package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.CourseInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCourseReviewFragment : AddReviewFragment() {
    private lateinit var reviewIndicationTitle: TextView

    private val courseReviewViewModel: CourseInfoViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reviewIndicationTitle = view.findViewById(R.id.reviewTitle)

        reviewIndicationTitle.text = getString(R.string.title_review, courseReviewViewModel.id)
    }

    override fun submitReview(): Boolean {
        val rating = addReviewViewModel.submitReview(courseReviewViewModel.id) ?: return false
        courseReviewViewModel.updateRating(rating)
        return true
    }
}