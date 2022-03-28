package com.github.sdp.ratemyepfl.fragment.review

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.AddReviewActivity
import com.github.sdp.ratemyepfl.adapter.ReviewAdapter
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.ListActivityUtils
import com.github.sdp.ratemyepfl.viewmodel.ReviewViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

open class ReviewListFragment : Fragment(R.layout.fragment_review_list) {
    private lateinit var reviewsAdapter: ReviewAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var fab: ExtendedFloatingActionButton
    private lateinit var swipeRefresher: SwipeRefreshLayout

    private val viewModel by activityViewModels<ReviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Floating action button for adding reviews
        fab = view.findViewById(R.id.startReviewFAB)

        // Vertical swipe refreshes the list of reviews
        swipeRefresher = view.findViewById(R.id.reviewSwipeRefresh)

        // List of reviews
        recyclerView = view.findViewById(R.id.reviewRecyclerView)

        reviewsAdapter = ReviewAdapter()
        recyclerView.adapter = reviewsAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        )

        recyclerView.setOnScrollListener(
            ListActivityUtils.createOnScrollListener(
                { fab.extend() },
                { fab.shrink() }
            )
        )

        swipeRefresher.setOnRefreshListener {
            viewModel.updateReviewsList()
            swipeRefresher.isRefreshing = false
        }

        setUpObservers()
    }

    private fun setUpObservers(){
        viewModel.getReviews().observe(viewLifecycleOwner) {
            it?.let {
                reviewsAdapter.submitList(it.toMutableList())
            }
        }

        viewModel.getReviewable().observe(viewLifecycleOwner){
            it?.let {reviewable ->
                fab.setOnClickListener {
                    startReview(reviewable)
                }
            }
        }
    }

    private fun startReview(reviewable: Reviewable) {
        val intent = Intent(requireView().context, AddReviewActivity::class.java)
        intent.putExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, reviewable.id)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateReviewsList()
    }
}
