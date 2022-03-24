package com.github.sdp.ratemyepfl.activity.course

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.github.sdp.ratemyepfl.activity.ReviewsListActivity
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.viewmodel.CourseReviewListViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity that displays the review of a given course
 */
@AndroidEntryPoint
class CourseReviewListActivity : ReviewsListActivity<Course>() {

    companion object {
        const val EXTRA_COURSE_JSON: String =
            "com.github.sdp.ratemyepfl.activity.course_json"
    }

    private val viewModel by viewModels<CourseReviewListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getReviews().observe(this) {
            it?.let {
                reviewsAdapter.submitList(it as MutableList<Review>)
            }
        }

        viewModel.course?.let { course ->
            fab.setOnClickListener {
                startReview(course, resultLauncher)
            }
        }

        swipeRefresher.setOnRefreshListener {
            viewModel.updateReviewsList()
            swipeRefresher.isRefreshing = false
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // refresh view model
                viewModel.updateReviewsList()
            }
        }
}