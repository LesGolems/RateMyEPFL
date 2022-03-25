package com.github.sdp.ratemyepfl.fragment.review

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.AddReviewActivity
import com.github.sdp.ratemyepfl.adapter.ReviewAdapter
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.ListActivityUtils
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

open class ReviewListFragment : Fragment(R.layout.fragment_review_list) {
    protected lateinit var reviewsAdapter: ReviewAdapter
    protected lateinit var recyclerView: RecyclerView

    protected lateinit var fab: ExtendedFloatingActionButton
    protected lateinit var swipeRefresher: SwipeRefreshLayout

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
    }

    fun startReview(reviewable: Reviewable) {
        val intent = Intent(requireView().context, AddReviewActivity::class.java)
        intent.putExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, reviewable.id)
        startActivity(intent)
    }
}
