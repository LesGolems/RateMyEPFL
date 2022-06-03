package com.github.sdp.ratemyepfl.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseOrderedQuery
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.backend.database.query.QueryResult
import com.github.sdp.ratemyepfl.backend.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.filter.ReviewableFilter

/**
 * A base class for [Reviewable] view models, that load offline data on creation. Use [elements] to
 * manage the data of the list.
 *
 */
sealed class ReviewableListViewModel<T : Reviewable>(
    private val repository: ReviewableRepository<T>,
    private val idFieldName: String,
    defaultFilter: ReviewableFilter<T>
) :
    ViewModel() {

    val elements: MutableLiveData<List<T>> = MutableLiveData(listOf())

    private var currentFilter: ReviewableFilter<T> = defaultFilter


    /**
     * Search for a prefix in a default field (corresponding to the [toString] definition).
     *
     * @see repository
     */
    open fun search(prefix: String, number: UInt = ReviewableRepository.LIMIT_QUERY_SEARCH) =
        postResult(repository.search(idFieldName, prefix, number))

    /**
     * Execute a load on the previous filter.
     *
     * @param number: Number of elements to load.
     *
     */
    open fun loadMore(number: UInt = FirebaseQuery.DEFAULT_QUERY_LIMIT): QueryResult<List<T>> =
        load(currentFilter, number)

    /**
     * Load elements according to the provided filter. It returns the elements loaded so far followed
     * by the new ones.
     *
     * @param filter: the filter to apply on the loading
     * @param number: the number of items to load
     *
     * @return a [QueryResult] containing the result
     */
    open fun load(
        filter: ReviewableFilter<T>,
        number: UInt = FirebaseQuery.DEFAULT_QUERY_LIMIT
    ): QueryResult<List<T>> {
        val query = fromFilter(filter)
        return postResult(repository.load(query, number), filter)
    }


    /**
     * Returns the elements loaded so far, or try to load new ones if none was loaded.
     *
     * @param filter: the filter to apply on the loading
     * @param number: the number of items to load
     *
     * @return a [QueryResult] containing the result
     */
    open fun loadIfAbsent(
        filter: ReviewableFilter<T>,
        number: UInt = FirebaseQuery.DEFAULT_QUERY_LIMIT
    ): QueryResult<List<T>> {
        val query = fromFilter(filter)
        val loaded = repository.loaded(query)
        return if (loaded != null) {
            postResult(QueryResult.success(loaded), filter)
        } else load(filter, number)
    }

    fun resetRepo() {
        repository.resetLoaded()
    }

    /**
     * Returns the elements loaded so far using the previous filter, or try to load new ones if none was loaded.
     *
     * @param number: the number of items to load
     *
     * @return a [QueryResult] containing the result
     */
    open fun loadIfAbsent(number: UInt = FirebaseQuery.DEFAULT_QUERY_LIMIT): QueryResult<List<T>> =
        loadIfAbsent(currentFilter, number)

    private fun fromFilter(filter: ReviewableFilter<T>): FirebaseOrderedQuery =
        filter.toQuery(repository.query())

    fun getCurrentFilter(): ReviewableFilter<T> = currentFilter

    /**
     * Post the results, i.e., actualise the displayed elements and the current filter
     */
    protected fun postResult(
        result: QueryResult<List<T>>,
        withFilter: ReviewableFilter<T>? = null
    ): QueryResult<List<T>> = result.mapResult {
        it.apply {
            if (withFilter != null) {
                currentFilter = withFilter
            }
            elements.postValue(it)
        }
    }


}