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
    data class Failure<T>(val error: Throwable) : QueryState<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failure(error: Throwable) = Failure<T>(error)
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
            failure(this.error)
        is Loading ->
            loading()
        is Success ->
            success(op(this.data))
    }

    /**
     * Map the error contained by the [Failure] with the provided operation.
     *
     * @param op: operation to apply on the content of the [Failure]
     */
    fun mapError(op: (Throwable) -> Throwable): QueryState<T> = when (this) {
        is Failure ->
            failure(op(this.error))
        else -> this
    }

    /**
     * Map the data and flatten it.
     *
     * @param op: operation that mapped the data to a [QueryState]
     */
    fun <U> flatMap(op: (T) -> QueryState<U>): QueryState<U> = when (this) {
        is Failure ->
            failure(this.error)
        is Loading ->
            loading()
        is Success -> op(this.data)
    }


}
