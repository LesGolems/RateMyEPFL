package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.adapter.ReviewableAdapter
import com.github.sdp.ratemyepfl.database.ReviewRepository
import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.ReviewableListViewModel
import com.github.sdp.ratemyepfl.viewmodel.filter.ReviewableFilter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class ReviewableTabFragment<T : Reviewable>(open val filterMenuId: Int) : Fragment(R.layout.layout_reviewable_list) {

    val reviewableAdapter = ReviewableAdapter { t -> displayReviews(t) }
    abstract val viewModel: ReviewableListViewModel<T>

    abstract val reviewActivityMenuId: Int
    abstract val reviewActivityGraphId: Int

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    private var isSearching: Boolean = false

    companion object {
        private val ON_TYPING_SEARCH_LIMIT = 2u
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.loading_progress_bar)
        progressText = view.findViewById(R.id.loading_progress_bar_text)
        recyclerView = view.findViewById(R.id.reviewableRecyclerView)
        recyclerView.adapter = reviewableAdapter

        setupRecyclerView(recyclerView)
        setupSearchBar(view)
        setupControls(view)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addItemDecoration(
            DividerItemDecoration(activity?.applicationContext, DividerItemDecoration.VERTICAL)
        )
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && !isSearching) {
                    displayResult(viewModel.loadMore())

                }
            }

        })
    }

    /**
     * Refresh the displayed list.
     */
    private fun refresh() {
        displayResult(viewModel.loadIfAbsent())
    }

    override fun onResume() {
        refresh()
        super.onResume()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        activity?.menuInflater
            ?.inflate(filterMenuId, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean =
    // If the fragment is not resumed, it should not modify it. This is achieved by returning
        // false here.
        if (isResumed) {
            when (item.itemId) {
                R.id.alphabeticOrder -> {
                    displayResult(viewModel.loadIfAbsent(alphabeticFilter()))
                    true
                }
                R.id.alphabeticOrderReversed -> {
                    displayResult(viewModel.loadIfAbsent(alphabeticFilter(true)))
                    true
                }
                R.id.bestRated -> {
                    displayResult(viewModel.loadIfAbsent(bestRatedFilter()))
                    true
                }
                R.id.worstRated -> {
                    displayResult(viewModel.loadIfAbsent(worstRatedFilter()))
                    true
                }
                else -> {
                    super.onContextItemSelected(item)
                }
            }
        } else false

    /**
     * Define the behavior for the control buttons
     *
     * @param view: View where the control are applied. The view must possess
     * the button [R.id.filterMenuButton]
     */
    private fun setupControls(view: View) {
        val filterButton = view.findViewById<Button>(R.id.filterMenuButton)
        registerForContextMenu(filterButton)
    }

    /**
     * Setup the search bar
     *
     * @param view: where the search bar is set up
     */
    private fun setupSearchBar(view: View) {
        val searchBar: SearchView = view.findViewById(R.id.reviewableSearchView)
        searchBar.setOnClickListener {
            startSearching(it as SearchView)
        }

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            private fun displaySearch(query: String?, number: UInt = ReviewableRepository.LIMIT_QUERY_SEARCH): Boolean =
                if (query != null) {
                    isSearching = true
                    displayResult(viewModel.search(query, number))
                    true
                } else false

            override fun onQueryTextSubmit(query: String?): Boolean = displaySearch(query)

            override fun onQueryTextChange(newText: String?): Boolean = displaySearch(newText, ON_TYPING_SEARCH_LIMIT)
        })

        searchBar.setOnQueryTextFocusChangeListener { _, b ->
            if (!b) {
                stopSearching(searchBar)
            } else startSearching(searchBar)
        }

        searchBar.setOnCloseListener {
            stopSearching(searchBar)
            true
        }
    }

    private fun startSearching(searchBar: SearchView) {
        searchBar.onActionViewExpanded()
        isSearching = true
    }

    private fun stopSearching(searchBar: SearchView) {
        searchBar.onActionViewCollapsed()
        displayResult(viewModel.loadIfAbsent())
        isSearching = false
    }

    /**
     * Starts a [ReviewActivity] for the corresponding [Reviewable].
     * @param reviewable: the reviewable for which we display the reviews
     */
    private fun displayReviews(reviewable: Reviewable) {
        val intent = Intent(activity?.applicationContext, ReviewActivity::class.java)
        intent.putExtra(
            ReviewActivity.EXTRA_ITEM_REVIEWED,
            reviewable.getId()
        )
        intent.putExtra(ReviewActivity.EXTRA_MENU_ID, reviewActivityMenuId)
        intent.putExtra(ReviewActivity.EXTRA_GRAPH_ID, reviewActivityGraphId)
        startActivity(intent)
    }

    /**
     * Describe the tabs that inherit this fragment
     *
     * @param tabName: Name of the tab that will be displayed on the [TabLayout]
     */
    enum class TAB(val tabName: String) {
        COURSE("Courses"),
        CLASSROOM("Classrooms"),
        RESTAURANT("Restaurants");

        /**
         * Return the corresponding fragment associated to the tab
         */
        fun toFragment() = when (this) {
            COURSE -> CourseTabFragment()
            CLASSROOM -> ClassroomTabFragment()
            RESTAURANT -> RestaurantTabFragment()
        }
    }

    /**
     * Display the result of a [QueryResult]. This must be call to update the view model, otherwise
     * the [Flow] is not terminated (and thus not executed).
     * This function is asynchronous and launch in the [viewModelScope].
     *
     * @param result: a [QueryResult] to display
     */
    fun displayResult(result: QueryResult<List<T>>) {
        viewModel.viewModelScope
            .launch {
                result.collect {
                    when (it) {
                        is QueryState.Failure -> {
                            stopLoading()
                            progressText.text = getString(R.string.database_error_loading_text)
                            Snackbar.make(
                                requireView(),
                                "An error occurred while loading the data",
                                Snackbar.LENGTH_LONG
                            )
                                .setAnchorView(recyclerView)
                                .show()
                        }
                        is QueryState.Loading -> startLoading()
                        is QueryState.Success -> {
                            stopLoading()
                        }
                    }
                }
            }
    }

    private fun startLoading() {
        recyclerView.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        progressText.visibility = View.VISIBLE
        progressText.text = getString(R.string.loading_progress_bar_text)

        progressBar.animate()
    }

    private fun stopLoading() {
        progressBar.visibility = View.INVISIBLE
        progressText.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
    }

    /**
     * Return the alphabetic order filter for the corresponding [T]
     */
    abstract fun alphabeticFilter(reverse: Boolean = false): ReviewableFilter<T>

    /**
     * Return the corresponding best rated filter
     */
    abstract fun bestRatedFilter(): ReviewableFilter<T>

    /**
     * Return the corresponding worst rated filter
     */
    abstract fun worstRatedFilter(): ReviewableFilter<T>
}
