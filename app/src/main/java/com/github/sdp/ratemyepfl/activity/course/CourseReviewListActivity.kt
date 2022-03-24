package com.github.sdp.ratemyepfl.activity.course

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
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.utils.ListActivityUtils.Companion.createOnScrollListener
import com.github.sdp.ratemyepfl.viewmodel.CourseReviewListViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity that displays the review of a given course
 */
@AndroidEntryPoint
class CourseReviewListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COURSE_JSON: String = "com.github.sdp.ratemyepfl.activity.course_json"
    }

    private val viewModel by viewModels<CourseReviewListViewModel>()

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
        viewModel.course?.let { course ->
            fab.setOnClickListener {
                startReview(course)
            }
        }

        // When the users scroll the list of reviews, the button shrinks
        recyclerView.setOnScrollListener(
            createOnScrollListener(
                { fab.extend() },
                { fab.shrink() }
            )
        )
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // refresh view model here
            }
        }

    private fun startReview(course: Course) {
        val intent = Intent(this, AddReviewActivity::class.java)
        intent.putExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, course.id)
        resultLauncher.launch(intent)
    }
}