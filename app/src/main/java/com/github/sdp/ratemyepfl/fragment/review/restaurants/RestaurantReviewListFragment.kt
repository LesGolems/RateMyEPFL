package com.github.sdp.ratemyepfl.fragment.review.restaurants

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.fragment.review.ReviewListFragment
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.viewmodel.RestaurantReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantReviewListFragment : ReviewListFragment() {
    private val viewModel by activityViewModels<RestaurantReviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getReviews().observe(viewLifecycleOwner) {
            it?.let {
                reviewsAdapter.submitList(it as MutableList<Review>)
            }
        }

        viewModel.getRestaurant().observe(viewLifecycleOwner){
            it?.let {course ->
                fab.setOnClickListener {
                    startReview(course)
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