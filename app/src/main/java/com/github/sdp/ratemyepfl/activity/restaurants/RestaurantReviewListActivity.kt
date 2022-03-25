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
import com.github.sdp.ratemyepfl.adapter.ReviewAdapter
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.utils.ListActivityUtils
import com.github.sdp.ratemyepfl.viewmodel.RestaurantReviewsListViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantReviewListActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_RESTAURANT_JSON: String =
            "com.github.sdp.ratemyepfl.activity.restaurants.extra_restaurant_json"
    }

    private val viewModel by viewModels<RestaurantReviewsListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_list)

        val reviewsAdapter = ReviewAdapter()
        val recyclerView: RecyclerView = findViewById(R.id.reviewRecyclerView)
        recyclerView.adapter = reviewsAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        )

        viewModel.getReviews().observe(this) {
            it?.let {
                reviewsAdapter.submitList(it as MutableList<Review>)
            }
        }

        // Floating action button for adding reviews
        val fab: ExtendedFloatingActionButton = findViewById(R.id.startCourseReviewFAB)
        viewModel.restaurant?.let { restaurant ->
            fab.setOnClickListener {
                addReview(restaurant)
            }
        }

        // When the users scroll the list of reviews, the button shrinks
        recyclerView.setOnScrollListener(
            ListActivityUtils.createOnScrollListener(
                { fab.extend() },
                { fab.shrink() }
            )
        )
    }

    private fun addReview(restaurant: Restaurant) {
        val intent = Intent(this, AddReviewActivity::class.java)
        intent.putExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, restaurant.id)
        resultLauncher.launch(intent)
    }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // refresh viewModel here
            }
        }

}