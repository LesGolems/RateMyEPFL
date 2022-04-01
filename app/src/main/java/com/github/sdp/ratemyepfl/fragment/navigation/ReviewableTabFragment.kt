package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.ListActivityUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
sealed class ReviewableTabFragment constructor(layout: Int) : Fragment(layout) {
    protected lateinit var reviewableAdapter: ReviewableAdapter
    protected lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.reviewableRecyclerView)

        reviewableAdapter = ReviewableAdapter { t -> displayReviews(t) }
        recyclerView.adapter = reviewableAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(activity?.applicationContext, DividerItemDecoration.VERTICAL)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(getMenuString(), menu)
        ListActivityUtils.setUpSearchView(menu, reviewableAdapter, getSearchViewString())
        super.onCreateOptionsMenu(menu, inflater)
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

    abstract fun getLayoutId(): Int

    /**
     * Creates an intent towards the ReviewActivity, it passes as extra the id of the reviewed item and
     * the id of the layout of the activity; we have one activity layout per reviewable item
     */
    private fun displayReviews(reviewable: Reviewable) {
        val intent = Intent(activity?.applicationContext, ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, Json.encodeToString<Reviewable>(reviewable))
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, getLayoutId())
        startActivity(intent)
    }

    companion object {
        const val NUMBER_OF_TABS = 3
        const val COURSE_TAB_NAME = "Course"
        const val CLASSROOM_TAB_NAME = "Classroom"
        const val RESTAURANT_TAB_NAME = "Restaurant"
        /**
         * Converts a position to the corresponding tab name
         *
         * @param position: index of the tab
         *
         * @return the corresponding name, (with COURSE_TAB_NAME as default)
         */
        fun fromPositionToTabName(position: Int): String = when(position) {
            0 -> COURSE_TAB_NAME
            1 -> CLASSROOM_TAB_NAME
            2 -> RESTAURANT_TAB_NAME
            else -> COURSE_TAB_NAME
        }

        /**
         * Converts a position to the corresponding tab fragment
         *
         * @param position: the tab index of the fragment
         *
         * @return the corresponding fragment (with CourseTabFragment as default)
         */
        fun fromPositionToFragment(position: Int): Fragment = when (position) {
            0 -> CourseTabFragment()
            2 -> RestaurantTabFragment()
            1 -> ClassroomTabFragment()
            else -> CourseTabFragment()
        }
    }
}
