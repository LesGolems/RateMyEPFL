package com.github.sdp.ratemyepfl.database.query

//typealias QueryResult<T> = Flow<QueryState<T>>

/**
 * Represents the state of a query. It can either be non-terminated (i.e., still loading), or
 * terminated (and either succeed or fail)
 */
sealed class QueryState<T> {
    /** A non-terminated query that is still waiting for some data */
    class Loading<T> : QueryState<T>()

    /** A terminated successful query that holds some data */
    data class Success<T>(val data: T) : QueryState<T>()

    /** A terminated failed query that holds an error message */
    data class Failure<T>(val errorMessage: String) : QueryState<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failure(error: String) = Failure<T>(error)
    }

    /**
     * Map the wrapped data.
     *
     * @param op: operation applied to the data
     *
     * @return a [QueryState] holding the mapped data
     */
    fun <U> map(op: (T) -> U): QueryState<U> = when (this) {
        is Failure ->
            failure(this.errorMessage)
        is Loading ->
            loading()
        is Success ->
            success(op(this.data))
    }

}