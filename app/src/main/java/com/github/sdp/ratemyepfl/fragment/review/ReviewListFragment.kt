package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ReviewAdapter
import com.github.sdp.ratemyepfl.database.ReviewRepository
import com.github.sdp.ratemyepfl.viewmodel.ReviewListViewModel

/*
Fragment for the list of reviews, shared among all reviewed items
 */
class ReviewListFragment : Fragment(R.layout.fragment_review_list) {
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresher: SwipeRefreshLayout

    // Gets the shared view model
    private val viewModel by activityViewModels<ReviewListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vertical swipe refreshes the list of reviews
        swipeRefresher = view.findViewById(R.id.reviewSwipeRefresh)

        // List of reviews
        recyclerView = view.findViewById(R.id.reviewRecyclerView)

        reviewAdapter = ReviewAdapter(
            { review -> viewModel.updateLikes(review) },
            { review -> viewModel.updateDislikes(review) }
        )

        recyclerView.adapter = reviewAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        )

        swipeRefresher.setOnRefreshListener {
            viewModel.updateReviewsList()
            swipeRefresher.isRefreshing = false
        }

        viewModel.reviews.observe(viewLifecycleOwner) {
            it?.let { reviewAdapter.submitList(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateReviewsList()
    }
}
