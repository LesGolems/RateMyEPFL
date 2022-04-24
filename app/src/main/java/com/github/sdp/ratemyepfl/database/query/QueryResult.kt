package com.github.sdp.ratemyepfl.database.query

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Wrap a [Flow] of [QueryState] to provide a simple abstraction. Delegation makes it behave as
 * the contained [Flow] (e.g., you can collect the value as with a [Flow])
 */
class QueryResult<T> (private val result: Flow<QueryState<T>>): Flow<QueryState<T>> by result {

    /**
     * Map the result of the query using a the provided operation
     *
     * @param op: operation to apply to the result
     *
     * @return a [QueryResult] with the mapped element
     */
    fun<U> mapResult(op: (T) -> U): QueryResult<U> =
        result.map { queryState ->
            queryState.map(op)
        }.asQueryResult()

    companion object {
        /**
         * Map each element of the list of results using the provided operation
         *
         * @param op: Operation to apply to each element
         *
         * @return a [QueryResult] containing a list of the mapped elements
         */
        fun<T, U> QueryResult<List<T>>.mapEach(op: (T) -> U): QueryResult<List<U>> =
            this.mapResult { list ->
                list.map(op)
            }

        /**
         * Wrap the flow in a [QueryResult]. This is a shorthand for a constructor call
         */
        fun<T> Flow<QueryState<T>>.asQueryResult(): QueryResult<T> =
            QueryResult(this)
    }
}