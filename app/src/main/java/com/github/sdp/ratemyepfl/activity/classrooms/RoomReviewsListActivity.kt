package com.github.sdp.ratemyepfl.activity.classrooms

import android.os.Bundle
import androidx.activity.viewModels
import com.github.sdp.ratemyepfl.activity.ReviewsListActivity
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.viewmodel.RoomReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity that displays the review of a given room
 */
@AndroidEntryPoint
class RoomReviewsListActivity : ReviewsListActivity<Classroom>() {

    companion object {
        const val EXTRA_CLASSROOMS_JSON =
            "com.github.sdp.ratemyepfl.activity.classrooms.extra_classrooms_json"
    }

    private val viewModel by viewModels<RoomReviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getReviews().observe(this) {
            it?.let {
                reviewsAdapter.submitList(it as MutableList<Review>)
            }
        }

        viewModel.room?.let { room ->
            fab.setOnClickListener {
                startReview(room)
            }
        }

        swipeRefresher.setOnRefreshListener {
            viewModel.updateReviewsList()
            swipeRefresher.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateReviewsList()
    }
}