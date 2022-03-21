package com.github.sdp.ratemyepfl.activity.classrooms

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.AddReviewActivity
import com.github.sdp.ratemyepfl.adapter.ReviewAdapter
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.utils.ListActivityUtils
import com.github.sdp.ratemyepfl.viewmodel.RoomReviewsListViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomReviewsListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CLASSROOMS_JSON = "com.github.sdp.ratemyepfl.activity.classrooms.extra_classrooms_json"
    }

    private val viewModel by viewModels<RoomReviewsListViewModel>()

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
        viewModel.room?.let { room ->
            fab.setOnClickListener {
                fabOnClick(room)
            }
        }

        recyclerView.setOnScrollListener(
            ListActivityUtils.createOnScrollListener(
                { fab.extend() },
                { fab.shrink() }
            )
        )
    }

    /* Adds review */
    private fun fabOnClick(room : Classroom) {
        val intent = Intent(this, AddReviewActivity::class.java)
        intent.putExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, room.id)
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // refresh viewModel here
            }
        }
}