package com.github.sdp.ratemyepfl.database.query

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.experimental.ExperimentalTypeInference

/**
 * It describes an simple abstraction on result of a [Query] depending on its state.
 * When the [Query] is started, the flow received a [QueryState.Loading]. It eventually
 * completes with either a [QueryState.Success] or fails with a [QueryState.Failure].
 *
 * Wrap a [Flow] of [QueryState] to provide a simple abstraction. Delegation makes it behave as
 * the contained [Flow] (e.g., you can collect the value as with a [Flow])
 *
 * *How to use it:*
 * ```
 * val queryResult: QueryResult
 * queryResult.collect {
 *      when (it) {
 *          is Success -> {}
 *          is Loading -> {}
 *          is Failure -> {}
 *      }
 * }
 * ```
 *
 */
class QueryResult<T>(private val result: Flow<QueryState<T>>) : Flow<QueryState<T>> by result {
    /**
     * Map the result of the query using a the provided operation
     *
     * @param op: operation to apply to the result
     *
     * @return a [QueryResult] with the mapped element
     */
    fun <U> mapResult(op: (T) -> U): QueryResult<U> =
        result.map { queryState ->
            queryState.map(op)
        }.asQueryResult()

    /**
     * Map the resulting error of the query using a the provided operation
     *
     * @param op: operation to apply to the result
     *
     * @return a [QueryResult] with the mapped element
     */
    fun mapError(op: (Throwable) -> Throwable): QueryResult<T> =
        result.map { queryState ->
            queryState.mapError(op)
        }.asQueryResult()


    /**
     * Defines a constructor that allows to build a [QueryResult] using the same syntax
     * as a [Flow]. It is equivalent to
     * ```
     * flow { ... }.asQueryResult()
     * ```
     *
     * A typical use-case is
     *
     * ```
     * QueryResult { collector ->
     *      emit(QueryState.loading())
     *      try {
     *          val data = someComputation()
     *          emit(QueryState.success(data))
     *      } catch (e: Exception) {
     *          emit(QueryState.failure(e)
     *      }
     * }
     * ```
     */
    @OptIn(ExperimentalTypeInference::class)
    constructor(@BuilderInference block: suspend FlowCollector<QueryState<T>>.() -> Unit) : this(flow { this.block() })

    companion object {
        /**
         * Map each element of the list of results using the provided operation
         *
         * @param op: Operation to apply to each element
         *
         * @return a [QueryResult] containing a list of the mapped elements
         */
        fun <T, U> QueryResult<List<T>>.mapEach(op: (T) -> U): QueryResult<List<U>> =
            this.mapResult { list ->
                list.map(op)
            }

        /**
         * Map each non-null documents of the [QuerySnapshot] with the provided operation.
         *
         * @param op: Operation to apply to each document
         *
         * @return a [QueryResult] containing a list of the mapped elements
         */
        fun <T> QueryResult<QuerySnapshot>.mapDocuments(op: (DocumentSnapshot) -> T?): QueryResult<List<T>> =
            this.mapResult { querySnapshot ->
                querySnapshot.mapNotNull(op)
            }


        /**
         * Wrap the flow in a [QueryResult]. This is a shorthand for a constructor call
         */
        fun <T> Flow<QueryState<T>>.asQueryResult(): QueryResult<T> =
            QueryResult(this)

        /**
         * Create a [QueryResult] that succeeds immediately. Must only be used for synchronous
         * operation.
         *
         * @param value: The successful result held by the [QueryResult]
         */
        fun <T> success(value: T): QueryResult<T> =
            flow<QueryState<T>> {
                emit(QueryState.success(value))
            }.asQueryResult()

        /**
         * Create a [QueryResult] that fails immediately. Must only be used for synchronous
         * operation.
         *
         * @param error: the error message held by the [QueryResult]
         */
        fun <T> failure(error: Throwable): QueryResult<T> =
            flow<QueryState<T>> {
                emit(QueryState.failure(error))
            }.asQueryResult()
    }
}