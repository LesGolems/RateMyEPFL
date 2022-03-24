package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.model.items.Reviewable

abstract class ReviewableListActivity<T : Reviewable> : AppCompatActivity() {

    protected lateinit var reviewableAdapter: ReviewableAdapter
    protected lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviewable_list)

        reviewableAdapter = ReviewableAdapter { t -> displayReviews(t as T) }
        recyclerView = findViewById(R.id.reviewableRecyclerView)
        recyclerView.adapter = reviewableAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        )

    }

    protected abstract fun displayReviews(t: T)
}