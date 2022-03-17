package com.github.sdp.ratemyepfl.activity.course

import android.content.Intent
import android.os.Bundle
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.CourseReview
import com.github.sdp.ratemyepfl.adapter.CourseReviewAdapter
import com.github.sdp.ratemyepfl.viewmodel.CourseReviewListViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Activity that displays the review of a given course
 */
@AndroidEntryPoint
class CourseReviewListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COURSE_JSON: String = "com.github.sdp.ratemyepfl.activity.course_json"
    }

    private lateinit var addReviewFAB: ExtendedFloatingActionButton

    private lateinit var reviewsView: ListView
    private val viewModel by viewModels<CourseReviewListViewModel>()
    private var course: Course? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_review_list)

        course = intent.getStringExtra(EXTRA_COURSE_JSON)
            ?.let { Json.decodeFromString(it) }

        addReviewFAB = findViewById(R.id.extended_fab)

        // If a course is given, we can review it
        course?.let { course ->
            addReviewFAB.setOnClickListener {
                startReview(course)
            }
        }

        reviewsView = findViewById(R.id.reviewsListView)

        // When the users scroll the list of reviews, the button shrinks
        reviewsView.setOnScrollListener(
            createOnScrollListener(
                { addReviewFAB.extend() },
                { addReviewFAB.shrink() }
            )
        )

        val reviewAdapter = CourseReviewAdapter(
            this, R.layout.list_reviews_row,
            // To be changed
            viewModel.getReviews() ?: listOf()
        )
        reviewsView.adapter = reviewAdapter
    }


    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val serializedReview = data?.getStringExtra(CourseReviewActivity.EXTRA_REVIEW)
                val serializedCourse = data?.getStringExtra(EXTRA_COURSE_JSON)
                if (serializedReview != null && serializedCourse != null) {
                    val review = CourseReview.deserialize(serializedReview)
                    val course = Json.decodeFromString<Course>(serializedCourse)
                    // NB: the view model and the adapter are not linked together
                    viewModel.database.value?.addReview(review)
                }
            }
        }


    private fun processResult(course: Course, review: CourseReview) {
        // Do some operations
    }

    private fun startReview(jsonCourse: String) {
        val intent = Intent(this, CourseReviewActivity::class.java)
        intent.putExtra(CourseReviewActivity.EXTRA_COURSE_IDENTIFIER, jsonCourse)
        resultLauncher.launch(intent)
    }

    private fun startReview(course: Course) {
        startReview(Json.encodeToString(course))
    }

    private fun createOnScrollListener(
        onScrollUp: () -> Unit,
        onScrollDown: () -> Unit
    ): OnScrollListener =
        object : OnScrollListener {
            private var lastFirstItem = 0
            override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {

            }

            override fun onScroll(p0: AbsListView?, firstItem: Int, p2: Int, p3: Int) {
                if (firstItem < lastFirstItem) {
                    onScrollUp()
                } else if (firstItem > lastFirstItem) {
                    onScrollDown()
                }

                lastFirstItem = firstItem
            }

        }

}