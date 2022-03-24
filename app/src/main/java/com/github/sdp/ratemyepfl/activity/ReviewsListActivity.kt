package com.github.sdp.ratemyepfl.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ReviewAdapter
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.utils.ListActivityUtils
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

open class ReviewsListActivity<T : Reviewable> (/*private val viewModel: ReviewsListViewModel*/) :
    AppCompatActivity() {

    protected lateinit var reviewsAdapter: ReviewAdapter
    protected lateinit var recyclerView: RecyclerView

    protected lateinit var fab: ExtendedFloatingActionButton
    protected lateinit var swipeRefresher: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_list)

        fab = findViewById(R.id.startReviewFAB)
        swipeRefresher = findViewById(R.id.swiperefresh)

        recyclerView = findViewById(R.id.reviewRecyclerView)
        recyclerView.addItemDecoration(
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        )

        reviewsAdapter = ReviewAdapter()
        recyclerView.adapter = reviewsAdapter


        /*viewModel.getReviews().observe(this) {
            it?.let {
                reviewsAdapter.submitList(it as MutableList<Review>)
            }
        }*/


        recyclerView.setOnScrollListener(
            ListActivityUtils.createOnScrollListener(
                { fab.extend() },
                { fab.shrink() }
            )
        )

        // Vertical swipe refreshes the list of reviews
        /*swipeRefresher.setOnRefreshListener {
            viewModel.refreshReviewsList()
            swipeRefresher.isRefreshing = false
        }*/
    }

    /*private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // refresh view model
                viewModel.refreshReviewsList()
            }
        }*/

    fun startReview(reviewable: Reviewable, resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(this, AddReviewActivity::class.java)
        intent.putExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, reviewable.id)
        resultLauncher.launch(intent)
    }
}