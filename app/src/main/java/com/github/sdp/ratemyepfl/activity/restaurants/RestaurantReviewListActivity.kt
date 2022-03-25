package com.github.sdp.ratemyepfl.activity.restaurants

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.AddReviewActivity
import com.github.sdp.ratemyepfl.activity.ReviewsListActivity
import com.github.sdp.ratemyepfl.adapter.ReviewAdapter
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.utils.ListActivityUtils
import com.github.sdp.ratemyepfl.viewmodel.RestaurantReviewsListViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantReviewListActivity : ReviewsListActivity<Restaurant>() {
    companion object {
        const val EXTRA_RESTAURANT_JSON: String =
            "com.github.sdp.ratemyepfl.activity.restaurants.extra_restaurant_json"
    }

    private val viewModel by viewModels<RestaurantReviewsListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getReviews().observe(this) {
            it?.let {
                reviewsAdapter.submitList(it as MutableList<Review>)
            }
        }

        viewModel.restaurant?.let { rest ->
            fab.setOnClickListener {
                startReview(rest)
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