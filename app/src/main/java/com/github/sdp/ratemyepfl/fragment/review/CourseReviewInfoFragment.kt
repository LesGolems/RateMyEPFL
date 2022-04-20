package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.viewmodel.CourseInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
Fragment displayed all relevant information for a Course
 */
@AndroidEntryPoint
class CourseReviewInfoFragment : Fragment(R.layout.fragment_course_review_info) {

    // Gets the shared view model
    private val viewModel by activityViewModels<CourseInfoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.course.observe(viewLifecycleOwner) {
            setUpCourseInfo(view, it)
        }
    }

    private fun getNumReviewString(numReview: Int): String {
        return if (numReview == 0) {
            getString(R.string.zero_num_reviews)
        } else {
            getString(R.string.num_reviews, numReview.toString())
        }
    }

    private fun setUpCourseInfo(view: View, course: Course) {
        view.findViewById<TextView>(R.id.courseId).text = course.courseCode
        view.findViewById<TextView>(R.id.courseTitle).text =
            getString(R.string.course_title, course.title)
        view.findViewById<TextView>(R.id.courseTeacher).text =
            getString(R.string.course_teacher, course.teacher)
        view.findViewById<TextView>(R.id.courseSection).text =
            getString(R.string.course_section, course.section)
        view.findViewById<TextView>(R.id.courseCredits).text =
            getString(R.string.course_credits, course.credits.toString())
        view.findViewById<TextView>(R.id.courseNumReview).text = getNumReviewString(course.numReviews)
        view.findViewById<RatingBar>(R.id.courseRatingBar).rating = course.averageGrade.toFloat()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateCourse()
    }
}