package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.viewmodel.RoomReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomReviewListFragment : ReviewListFragment() {
    companion object {
        const val EXTRA_CLASSROOMS_JSON =
            "com.github.sdp.ratemyepfl.activity.classrooms.extra_classrooms_json"
    }

    private val viewModel by activityViewModels<RoomReviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getReviews().observe(viewLifecycleOwner) {
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