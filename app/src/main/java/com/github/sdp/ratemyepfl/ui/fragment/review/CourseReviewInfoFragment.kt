package com.github.sdp.ratemyepfl.ui.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.utils.InfoFragmentUtils.getNumReviewString
import com.github.sdp.ratemyepfl.viewmodel.review.CourseInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
Fragment displayed all relevant information for a Course
 */
@AndroidEntryPoint
class CourseReviewInfoFragment : Fragment(R.layout.fragment_course_review_info) {

    // Gets the shared view model
    private val viewModel by activityViewModels<CourseInfoViewModel>()

    private lateinit var courseCode: TextView
    private lateinit var courseTitle: TextView
    private lateinit var courseTeacher: TextView
    private lateinit var courseSection: TextView
    private lateinit var courseCredits: TextView
    private lateinit var courseNumReview: TextView
    private lateinit var courseRatingBar: RatingBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        courseCode = view.findViewById(R.id.courseCode)
        courseTitle = view.findViewById(R.id.courseTitle)
        courseTeacher = view.findViewById(R.id.courseTeacher)
        courseSection = view.findViewById(R.id.courseSection)
        courseCredits = view.findViewById(R.id.courseCredits)
        courseNumReview = view.findViewById(R.id.courseNumReview)
        courseRatingBar = view.findViewById(R.id.courseRatingBar)

        viewModel.course.observe(viewLifecycleOwner) {
            setUpCourseInfo(it)
        }
    }

    private fun setUpCourseInfo(course: Course) {
        courseCode.text = course.courseCode
        courseTitle.text = course.title
        courseTeacher.text = course.teacher
        courseSection.text = course.section
        courseCredits.text = course.credits.toString()
        courseNumReview.text = getNumReviewString(requireContext(), course.numReviews)
        courseRatingBar.rating = course.grade.toFloat()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateCourse()
    }
}