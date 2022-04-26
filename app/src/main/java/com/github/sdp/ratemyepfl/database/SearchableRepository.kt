package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.query.QueryResult

interface SearchableRepository<T> {

    /**
     * Search for a given pattern in the repository. It matches at most 5 elements.
     *
     * @param prefix: the beginning of the word to match
     *
     * @return a [QueryResult] of the matched words
     */
    fun search(prefix: String): QueryResult<List<T>>

    companion object {
        const val LIMIT_QUERY_SEARCH = 5u
    }
}