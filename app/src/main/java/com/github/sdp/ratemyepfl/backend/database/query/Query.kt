package com.github.sdp.ratemyepfl.backend.database.query

import com.github.sdp.ratemyepfl.exceptions.DatabaseException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import kotlin.math.min

typealias FirebaseQuery = com.google.firebase.firestore.Query

/**
 * Create an executable query that is limited in the number of fetched document. It can retrieve
 * up to **50** documents.
 *
 * If the [Query] fails during the execution, it returns
 * a [DatabaseException].
 */
data class Query(private val query: FirebaseQuery) {

    companion object {
        const val MAX_QUERY_LIMIT = 50u
        const val DEFAULT_QUERY_LIMIT = 10u
    }

    /**
     * Filter the result by equality (=) on a given field, under the condition that the field exists
     *
     * @param field: the field to filter
     * @param value: the value to filter with
     *
     * @return a filtered [Query]
     */
    fun whereEqualTo(field: String, value: Any) =
        Query(query.whereEqualTo(field, value))

    /**
     * Filter the result by comparison (>) on a given field, under the condition that the field exists
     *
     * @param field: the field to filter
     * @param value: the value to filter with
     *
     * @return a filtered [Query]
     */
    fun whereGreaterThan(field: String, value: Any) =
        Query(query.whereGreaterThan(field, value))

    /**
     * Filter the result by comparison (>=) on a given field, under the condition that the field exists
     *
     * @param field: the field to filter
     * @param value: the value to filter with
     *
     * @return a filtered [OrderedQuery]
     */
    fun whereGreaterThanOrEqualTo(field: String, value: Any) =
        Query(query.whereGreaterThanOrEqualTo(field, value))

    /**
     * Filter the result by comparison (<) on a given field, under the condition that the field exists
     *
     * @param field: the field to filter
     * @param value: the value to filter with
     *
     * @return a filtered [Query]
     */
    fun whereLessThan(field: String, value: Any) =
        Query(query.whereLessThan(field, value))

    /**
     * Filter the result by comparison (<=) on a given field, under the condition that the field exists
     *
     * @param field: the field to filter
     * @param value: the value to filter with
     *
     * @return a filtered [Query]
     */
    fun whereLessThanOrEqualTo(field: String, value: Any) =
        Query(query.whereLessThanOrEqualTo(field, value))

    /**
     * Creates and returns a new Query with the additional filter that documents must contain the
     * specified field and the value does not equal the specified value. A Query can have only one
     * [whereNotEqualTo] filter, and it cannot be combined with [whereNotIn].
     *
     * ***NOTE: For some reason, executing it returns an additional document with garbage data. Please
     * take this into account when you convert the result.***
     *
     * @param field: the field to filter
     * @param value: the value to filter with
     *
     * @return a filtered [Query]
     */
    fun whereNotEqualTo(field: String, value: Any) =
        Query(query.whereNotEqualTo(field, value))

    /**
     * Creates and returns a new Query with the additional filter that documents must contain the
     * specified field and the value must equal one of the values from the provided list.
     * A Query can have only one [whereIn] filter, and it cannot be combined with
     * [whereArrayContainsAny].
     *
     * @param field: the field to filter
     * @param values: the values to filter with
     *
     * @return a filtered [Query]
     */
    fun whereIn(field: String, values: List<Any>) =
        Query(query.whereIn(field, values))

    /**
     * Creates and returns a new Query with the additional filter that documents must contain the
     * specified field and the value does not equal any of the values from the provided list.
     * One special case is that whereNotIn cannot match null values. To query for documents where a
     * field exists and is null, use whereNotEqualTo, which can handle this special case.
     * A Query can have only one whereNotIn() filter, and it cannot be combined with
     * [whereArrayContains], [whereArrayContainsAny], [whereIn], or [whereNotEqualTo].
     *
     * @param field: the field to filter
     * @param values: the values to filter with
     *
     * @return a filtered [Query]
     */
    fun whereNotIn(field: String, values: List<Any>) =
        Query(query.whereNotIn(field, values))

    /**
     * Creates and returns a new Query with the additional filter that documents must contain the
     * specified field, the value must be an array, and that the array must contain the provided
     * value. A [Query] can have only one [whereArrayContains] filter and it cannot be combined
     * with [whereArrayContainsAny].
     *
     * @param field: the field to filter
     * @param value: the value to filter with
     *
     * @return a filtered [Query]
     */
    fun whereArrayContains(field: String, value: Any) =
        Query(query.whereArrayContains(field, value))

    /**
     * Creates and returns a new Query with the additional filter that documents must contain the
     * specified field, the value must be an array, and that the array must contain the provided
     * value. A [Query] can have only one [whereArrayContainsAny] filter and it cannot be combined
     * with [whereArrayContains] or [whereIn].
     *
     * @param field: the field to filter
     * @param values: the values to filter with
     *
     * @return a filtered [Query]
     */
    fun whereArrayContainsAny(field: String, values: List<Any>) =
        Query(query.whereArrayContainsAny(field, values))

    /**
     * Execute the query and returns the result as a flow. This limit the number of item
     * retrieved from the database.
     *
     * @param limit: the maximal number of item to retrieved, [DEFAULT_QUERY_LIMIT] by default.
     * Cannot exceed [MAX_QUERY_LIMIT]
     *
     * @return a flow containing the result wrapped inside a [QueryState]
     */
    fun execute(
        limit: UInt = DEFAULT_QUERY_LIMIT,
    ): QueryResult<QuerySnapshot> =
        QueryResult {
            val nbr = min(limit, MAX_QUERY_LIMIT).toLong()
            // Execute the query
            val result: QuerySnapshot = query.limit(nbr)
                .get()
                .await()

            emit(QueryState.success(result))
        }.mapError { error ->
            DatabaseException("Failed to execute the query $this. (From $error \n ${error.stackTrace}")
        }

    /**
     * Creates and returns a new Query with the additional filter that documents must contain the
     * specified field, and ordered on the provided field.
     *
     * @param field: the field to order
     * @param direction: the direction f the order, ascending by default
     *
     * @return an [OrderedQuery]
     */
    fun orderBy(
        field: String,
        direction: com.google.firebase.firestore.Query.Direction = com.google.firebase.firestore.Query.Direction.ASCENDING
    ): OrderedQuery =
        OrderedQuery(query.orderBy(field, direction), OrderedQuery.OrderedField(field, direction))

    /**
     * Return a new [OrderedQuery] where the documents matches a given prefix in a given field.
     *
     * @param field: the field to match
     * @param prefix: the prefix that the value must match
     */
    fun match(field: String, prefix: String) = orderBy(field)
        .match(prefix)
}
