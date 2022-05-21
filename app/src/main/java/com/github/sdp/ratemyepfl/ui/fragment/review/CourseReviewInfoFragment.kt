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

    private lateinit var courseId: TextView
    private lateinit var courseTitle: TextView
    private lateinit var courseTeacher: TextView
    private lateinit var courseSection: TextView
    private lateinit var courseCredits: TextView
    private lateinit var courseNumReview: TextView
    private lateinit var courseRatingBar: RatingBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        courseId = view.findViewById(R.id.courseId)
        courseTitle = view.findViewById(R.id.courseTitle)
        courseTeacher = view.findViewById(R.id.courseTeacher)
        courseSection = view.findViewById(R.id.courseSection)
        courseCredits = view.findViewById(R.id.courseCredits)
        courseNumReview = view.findViewById(R.id.courseNumReview)
        courseRatingBar = view.findViewById(R.id.courseRatingBar)

        viewModel.course.observe(viewLifecycleOwner) {
            setUpCourseInfo(view, it)
        }
    }

    private fun setUpCourseInfo(view: View, course: Course) {
        courseId.text = course.courseCode
        courseTitle.text =
            getString(R.string.course_title, course.title)
        courseTeacher.text =
            getString(R.string.course_teacher, course.teacher)
        courseSection.text =
            getString(R.string.course_section, course.section)
        courseCredits.text =
            getString(R.string.course_credits, course.credits.toString())
        courseNumReview.text =
            getNumReviewString(requireContext(), course.numReviews)
        courseRatingBar.rating = course.grade.toFloat()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateCourse()
    }
}