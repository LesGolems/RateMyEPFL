package com.github.sdp.ratemyepfl.activity.course

import android.os.Bundle
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.CourseReviewAdapter
import com.github.sdp.ratemyepfl.viewmodel.ReviewListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseReviewListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COURSE_NAME: String = "com.github.sdp.ratemyepfl.activity.course_name"
    }

    private lateinit var reviewsView: ListView
    private val viewModel by viewModels<ReviewListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        val reviewsList = viewModel.getReviews()

        reviewsView = findViewById(R.id.reviewsListView)
        val reviewAdapter = CourseReviewAdapter(this, R.layout.list_reviews_row,
            reviewsList)
        reviewsView.adapter = reviewAdapter
    }
}