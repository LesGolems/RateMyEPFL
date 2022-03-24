package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ReviewAdapter
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.ListActivityUtils
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

open class ReviewsListActivity<T : Reviewable> : AppCompatActivity() {

    protected lateinit var reviewsAdapter: ReviewAdapter
    protected lateinit var recyclerView: RecyclerView

    protected lateinit var fab: ExtendedFloatingActionButton
    protected lateinit var swipeRefresher: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_list)

        // Floating action button for adding reviews
        fab = findViewById(R.id.startReviewFAB)

        // Vertical swipe refreshes the list of reviews
        swipeRefresher = findViewById(R.id.swiperefresh)

        // List of reviews
        fab = findViewById(R.id.startReviewFAB)
        swipeRefresher = findViewById(R.id.swiperefresh)

        recyclerView = findViewById(R.id.reviewRecyclerView)
        recyclerView.addItemDecoration(
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        )

        reviewsAdapter = ReviewAdapter()
        recyclerView.adapter = reviewsAdapter

        recyclerView.setOnScrollListener(
            ListActivityUtils.createOnScrollListener(
                { fab.extend() },
                { fab.shrink() }
            )
        )
    }

    fun startReview(reviewable: Reviewable, resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(this, AddReviewActivity::class.java)
        intent.putExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, reviewable.id)
        resultLauncher.launch(intent)
    }
}