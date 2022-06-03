package com.github.sdp.ratemyepfl.backend.database

import com.github.sdp.ratemyepfl.backend.database.query.FirebaseOrderedQuery
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.backend.database.query.QueryResult

interface LoaderRepository<T : RepositoryItem> : Repository<T> {

    /**
     * Execute the query and load elements. If a query was already executed
     * before-hand, it load subsequent data (in the order of the query).
     * The result contains the total number of item loaded since the
     * beginning (e.g, if you already loaded 5 elements, it returns 7 elements when
     * you attempt to load 2 more elements)
     *
     * @param query: the query to execute. It must be ordered.
     * @param number: the number of items to load
     *
     * @return a [QueryResult] containing the result
     */
    fun load(
        query: FirebaseOrderedQuery,
        number: UInt = FirebaseQuery.DEFAULT_QUERY_LIMIT
    ): QueryResult<List<T>>

    fun resetLoaded()

    /**
     * Returns the loaded element so far for a given query.
     *
     * @param query: the query for which we want to retrieve data
     *
     * @return the list of data loaded or null if no data were loaded.
     */
    fun loaded(query: FirebaseOrderedQuery): List<T>?
}