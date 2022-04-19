package com.github.sdp.ratemyepfl.database

import com.google.firebase.firestore.Query

interface LoaderRepository<T> {

    /**
     * Execute the query and load elements. If a query was already executed
     * before-hand, it load subsequent data (in the order of the query).
     * The result contains the total number of item loaded since the
     * beginning (e.g, if you already loaded 5 elements, it returns 7 elements when
     * you attempt to load 2 more elements)
     *
     * @param query: the query to execute
     * @param number: the number of items to load
     *
     * @return a [QueryResult] containing the result
     */
    fun load(query: Query, number: Long): QueryResult<List<T>>
}