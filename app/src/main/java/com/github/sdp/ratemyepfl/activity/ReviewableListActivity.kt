package com.github.sdp.ratemyepfl.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.ListActivityUtils

abstract class ReviewableListActivity<T : Reviewable> : AppCompatActivity() {

    protected lateinit var reviewableAdapter: ReviewableAdapter
    protected lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviewable_list)

        recyclerView = findViewById(R.id.reviewableRecyclerView)

        reviewableAdapter = ReviewableAdapter { t -> displayReviews(t as T) }
        recyclerView.adapter = reviewableAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(getMenuString(), menu)
        ListActivityUtils.setUpSearchView(menu, reviewableAdapter, getSearchViewString())
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.increasingOrder -> {
            reviewableAdapter.sortAlphabetically(true)
            true
        }
        R.id.decreasingOrder -> {
            reviewableAdapter.sortAlphabetically(false)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    abstract fun getMenuString(): Int

    abstract fun getSearchViewString(): Int

    abstract fun displayReviews(t: T)
}