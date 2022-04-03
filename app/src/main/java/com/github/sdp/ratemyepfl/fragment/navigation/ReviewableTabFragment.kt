package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SearchView
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
    private lateinit var filterButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.reviewableRecyclerView)
        recyclerView.adapter = reviewableAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(activity?.applicationContext, DividerItemDecoration.VERTICAL)
        )

        filterButton = view.findViewById(R.id.filterMenuButton)
        searchBar = view.findViewById(R.id.reviewableSearchView)
        setupControls()
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
            super.onContextItemSelected(item)
        }
    }

    private fun setupControls() {
        registerForContextMenu(filterButton)
        var sorted = true
        filterButton.setOnClickListener {
            reviewableAdapter.sortAlphabetically(sorted) {
                recyclerView.scrollToPosition(0)
            }
            sorted = !sorted
        }

        searchBar.setOnClickListener { (it as SearchView).onActionViewExpanded() }
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerView.scrollToPosition(0)
                reviewableAdapter.filter.filter(newText)
                return true
            }
        })

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

    enum class TAB(name: String) {
        COURSE("Course"),
        CLASSROOM("Classroom"),
        RESTAURANT("Restaurant");

        fun toFragment() = when(this) {
            COURSE -> CourseTabFragment()
            CLASSROOM -> ClassroomTabFragment()
            RESTAURANT -> RestaurantTabFragment()
        }
    }
}
