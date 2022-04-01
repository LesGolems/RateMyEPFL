package com.github.sdp.ratemyepfl.fragment.navigation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.model.items.Reviewable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

abstract class ReviewableTabFragment : Fragment(R.layout.layout_reviewable_list) {

    val reviewableAdapter = ReviewableAdapter { t -> displayReviews(t) }

    open val filterMenuId: Int = R.menu.default_filter_menu
    abstract val reviewActivityLayoutId: Int

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: SearchView
    private lateinit var filterMenuButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.reviewableRecyclerView)
        recyclerView.adapter = reviewableAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(activity?.applicationContext, DividerItemDecoration.VERTICAL)
        )

        filterMenuButton = view.findViewById(R.id.filterMenuButton)
        registerForContextMenu(filterMenuButton)

        searchBar = view.findViewById(R.id.reviewableSearchView)
        setupSearch()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        activity?.menuInflater?.inflate(filterMenuId, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }


    override fun onContextItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.increasingOrder -> {
            reviewableAdapter.sortAlphabetically()
            true
        }
        R.id.decreasingOrder -> {
            reviewableAdapter.sortAlphabetically(true)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSearch() {
        searchBar.setOnClickListener { (it as SearchView).onActionViewExpanded() }
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                reviewableAdapter.filter.filter(newText)
                return true
            }
        })

        // Collapse the search bar when the users clicks on the filter button
        //onTouchCollapseSearchBar(filterMenuButton)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onTouchCollapseSearchBar(view: View) {
        if (view !is SearchView) {
            view.setOnTouchListener { _, _ ->
                searchBar.onActionViewCollapsed()
                true
            }
        }
    }

    /**
     * Starts a [ReviewActivity] for the corresponding [Reviewable].
     * @param reviewable: the reviewable for which we display the reviews
     */
    private fun displayReviews(reviewable: Reviewable) {
        val intent = Intent(activity?.applicationContext, ReviewActivity::class.java)
        intent.putExtra(
            ReviewActivity.EXTRA_ITEM_REVIEWED,
            Json.encodeToString<Reviewable>(reviewable)
        )
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, reviewActivityLayoutId)
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
        fun fromPositionToTabName(position: Int): String = when (position) {
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
