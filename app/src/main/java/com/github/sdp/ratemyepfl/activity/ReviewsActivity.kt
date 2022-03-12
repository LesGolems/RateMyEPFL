package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.FakeCoursesDatabase
import com.github.sdp.ratemyepfl.model.review.CourseReviewAdapter

class ReviewsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COURSE_NAME: String = "course name"
    }

    private lateinit var reviewsView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        val fakeDatabase = FakeCoursesDatabase()
        val reviewsList = fakeDatabase.getReviewsList()

        reviewsView = findViewById(R.id.reviewsListView)
        val reviewAdapter = CourseReviewAdapter(this, R.layout.list_reviews_row,
            reviewsList)
        reviewsView.adapter = reviewAdapter
    }
}