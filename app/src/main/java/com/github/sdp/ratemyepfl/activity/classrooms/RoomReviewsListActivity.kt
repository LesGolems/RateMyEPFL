package com.github.sdp.ratemyepfl.activity.classrooms

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.github.sdp.ratemyepfl.activity.ReviewsListActivity
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.viewmodel.RoomReviewsListViewModel
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

    private val viewModel by viewModels<RoomReviewsListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getReviews().observe(this) {
            it?.let {
                reviewsAdapter.submitList(it as MutableList<Review>)
            }
        }

        // Floating action button for adding reviews
        viewModel.room?.let { room ->
            fab.setOnClickListener {
                startReview(room, resultLauncher)
            }
        }

        // Vertical swipe refreshes the list of reviews
        swipeRefresher.setOnRefreshListener {
            viewModel.updateReviewsList()
            swipeRefresher.isRefreshing = false
        }

    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // refresh viewmodel
                viewModel.updateReviewsList()
            }
        }
}