package com.github.sdp.ratemyepfl.fragment.review.classroom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.fragment.review.ReviewListFragment
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomReviewListFragment : ReviewListFragment() {
    private val viewModel by activityViewModels<ReviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getReviews().observe(viewLifecycleOwner) {
            it?.let {
                reviewsAdapter.submitList(it as MutableList<Review>)
            }
        }

        viewModel.getReviewable().observe(viewLifecycleOwner){
            it?.let {room ->
                fab.setOnClickListener {
                    startReview(room)
                }
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