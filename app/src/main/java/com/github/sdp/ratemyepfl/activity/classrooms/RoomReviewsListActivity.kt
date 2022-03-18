package com.github.sdp.ratemyepfl.activity.classrooms

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.AddReviewActivity
import com.github.sdp.ratemyepfl.adapter.RoomReviewsAdapter
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.viewmodel.RoomReviewsListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomReviewsListActivity : AppCompatActivity() {

    private val viewModel: RoomReviewsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_reviews_list)

        val reviewsAdapter = RoomReviewsAdapter()
        val recyclerView: RecyclerView = findViewById(R.id.review_recycler_view)
        recyclerView.adapter = reviewsAdapter

        // Display the reviews of the classroom
        viewModel.id.let {
            viewModel.getReviews().observe(this) {
                it?.let {
                    reviewsAdapter.submitList(it as MutableList<Review>)
                }
            }
        }

        // Floating action button for adding reviews
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            fabOnClick()
        }

    }

    /* Adds review */
    private fun fabOnClick() {
        val intent = Intent(this, AddReviewActivity::class.java)
        intent.putExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, viewModel.id)
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // refresh viewModel here
            }
        }
}