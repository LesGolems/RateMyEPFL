package com.github.sdp.ratemyepfl.backend.database.query

import com.github.sdp.ratemyepfl.exceptions.QueryExecutionException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeout
import kotlin.experimental.ExperimentalTypeInference

/**
 * It describes an simple abstraction on result of a [FirebaseQuery] depending on its state.
 * When the [FirebaseQuery] is started, the flow received a [QueryState.Loading]. It either
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
 * NB: A [QueryResult] is guaranteed to receive either a [QueryState.Success] or a [QueryState.Failure]
 * at some point in time.
 *
 * *How to transform the result:*
 * @see mapResult
 * @see mapError
 * @see Flow
 */
class QueryResult<T> private constructor(private val result: Flow<QueryState<T>>) :
    Flow<QueryState<T>> by result {
    /**
     * Defines a constructor that allows to build a [QueryResult] from a suspendable computation.
     * It emits a [QueryState.Loading] in the beginning and wrap any [Throwable]
     * in a [QueryState.Failure].
     *
     * ***NB***: To succeed or fail *instantaneously*, use the provided [success] and [failure].
     *
     * The flow will receive at most 2 values in the following order:
     * - A [QueryState.Loading] in the beginning.
     * - A [QueryState.Success] if the computation succeeds, or a [QueryState.Failure] if an exception
     * is thrown.
     *
     * A typical use-case is
     *
     * ```
     * QueryResult { collector ->
     *      val data = someComputation()
     *      data
     * }
     * ```
     *
     * To personalized the [QueryState.Failure], use a try-catch around throwing code.
     */
    @OptIn(ExperimentalTypeInference::class)
    constructor(@BuilderInference computation: suspend () -> T) : this(
        flow {
            emit(QueryState.loading())
            val result = computation()
            emit(QueryState.success(result))
        }.catch { cause ->
            emit(QueryState.failure(cause))
        }
    )

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
        QueryResult(result.map { queryState ->
            queryState.mapError(op)
        })

    fun <U> map(op: (QueryState<T>) -> QueryState<U>) =
        QueryResult(result.map(op))

    /**
     * This function behaves like await(), and waits for the completion of the query.
     *
     * @return the result of the query, if it succeeds
     *
     * It propagates the source of the failure it it fails.
     */
    suspend fun collectResult(): T {
        var result: T? = null
        this@QueryResult.collect {
            when (it) {
                is QueryState.Failure -> throw it.error
                is QueryState.Loading -> {} // Wait for the result
                is QueryState.Success -> result = it.data
            }
        }
        // By construction, the flow will either throw an exception or remove the nullability
        // of the result
        return result
            ?: throw QueryExecutionException("The query should return a result, which is not the case")
    }

    /**
     * Collect the result, or throws a [TimeoutCancellationException] if it does not complete in
     * [duration] milliseconds.
     *
     * @param duration: the duration of the timeout in milliseconds
     * @throws TimeoutCancellationException: if it does not succeed in a given amount of time
     */
    suspend fun collectResultWithTimeout(duration: Long = COLLECT_RESULT_TIMEOUT_MS) =
        withTimeout(duration) {
            collectResult()
        }

    /**
     *  Transform the result into a Success containing a default value if the error thrown is of
     *  type [E]. It can be used if we want to provide a success if the catch error has a given type.
     *
     *  @param default: the default value to return
     *
     */
    inline fun <reified E : Exception> withDefault(default: T): QueryResult<T> =
        this.map {
            if (it is QueryState.Failure && it.error is E)
                QueryState.success(default)
            else it
        }

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
         * Flatten a sequence of [QueryResult]. It either succeed if each result succeeds, or fails
         * with the first failure.
         */
        fun <T> Iterable<QueryResult<T>>.flatten(): QueryResult<List<T>> {
            val withoutLoading = this.map {
                it.mapResult { result -> listOf(result) }
                    .filter { state -> state !is QueryState.Loading }
            }

            val res = QueryResult(withoutLoading.reduce { acc, flow ->
                val first = acc as QueryResult<List<T>>
                first.zip(flow) { state1, state2 ->
                    when (state1) {
                        is QueryState.Failure -> state1
                        is QueryState.Loading -> state2
                        is QueryState.Success -> when (state2) {
                            is QueryState.Failure -> state2
                            is QueryState.Loading -> state1
                            is QueryState.Success -> QueryState.success(state1.data.plus(state2.data))
                        }
                    }
                }
            })

            return QueryResult {
                res.collectResult()
            }
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

        const val COLLECT_RESULT_TIMEOUT_MS = 10000L

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