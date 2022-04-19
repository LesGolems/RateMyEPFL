package com.github.sdp.ratemyepfl.database

interface SearchableRepository<T> {

    /**
     * Search for a given pattern in the repository. It matches at most 5 elements.
     *
     * @param pattern: the beginning of the word to match
     *
     * @return a [QueryResult] of the matched words
     */
    fun search(pattern: String): QueryResult<List<T>>

    companion object {
        const val LIMIT_QUERY_SEARCH: Long = 5
    }
}