package com.github.sdp.ratemyepfl.database.query

import com.github.sdp.ratemyepfl.database.query.Query.Companion.DEFAULT_QUERY_LIMIT
import com.github.sdp.ratemyepfl.database.query.Query.Companion.MAX_QUERY_LIMIT
import com.google.firebase.firestore.QuerySnapshot


/**
 * A query ordered on provided fields
 */
data class OrderedQuery(private val query: FirebaseQuery, val fields: List<String>) {

    private fun orderedQuery(query: FirebaseQuery) =
        OrderedQuery(query, fields)

    constructor(query: FirebaseQuery, vararg fields: String) : this(query, fields.toList())

    companion object {
        private const val SUFFIX_MATCHER = "\uf8ff"
    }

    /**
     * Filter the result by equality (=) on a given field, under the condition that the field exists
     *
     * @param field: the field to filter
     * @param value: the value to filter with
     *
     * @return a filtered [OrderedQuery]
     */
    fun whereEqualTo(field: String, value: Any) =
        orderedQuery(query.whereEqualTo(field, value))

    /**
     * Filter the result by comparison (>) on a given field, under the condition that the field exists
     *
     * @param field: the field to filter
     * @param value: the value to filter with
     *
     * @return a filtered [OrderedQuery]
     */
    fun whereGreaterThan(field: String, value: Any) =
        orderedQuery(query.whereGreaterThan(field, value))

    /**
     * Filter the result by comparison (>=) on a given field, under the condition that the field exists
     *
     * @param field: the field to filter
     * @param value: the value to filter with
     *
     * @return a filtered [OrderedQuery]
     */
    fun whereGreaterThanOrEqualTo(field: String, value: Any) =
        orderedQuery(query.whereGreaterThanOrEqualTo(field, value))

    /**
     * Filter the result by comparison (<) on a given field, under the condition that the field exists
     *
     * @param field: the field to filter
     * @param value: the value to filter with
     *
     * @return a filtered [OrderedQuery]
     */
    fun whereLessThan(field: String, value: Any) =
        orderedQuery(query.whereLessThan(field, value))

    /**
     * Filter the result by comparison (<=) on a given field, under the condition that the field exists
     *
     * @param field: the field to filter
     * @param value: the value to filter with
     *
     * @return a filtered [OrderedQuery]
     */
    fun whereLessThanOrEqualTo(field: String, value: Any) =
        orderedQuery(query.whereLessThanOrEqualTo(field, value))

    /**
     * Creates and returns a new Query with the additional filter that documents must contain the
     * specified field and the value does not equal the specified value. A Query can have only one
     * [whereNotEqualTo] filter, and it cannot be combined with [whereNotIn].
     *
     * @param field: the field to filter
     * @param value: the value to filter with
     *
     * @return a filtered [OrderedQuery]
     */
    fun whereNotEqualTo(field: String, value: Any) =
        orderedQuery(query.whereNotEqualTo(field, value))

    /**
     * Creates and returns a new Query with the additional filter that documents must contain the
     * specified field and the value must equal one of the values from the provided list.
     * A Query can have only one [whereIn] filter, and it cannot be combined with
     * [whereArrayContainsAny].
     *
     * @param field: the field to filter
     * @param values: the values to filter with
     *
     * @return a filtered [OrderedQuery]
     */
    fun whereIn(field: String, values: List<Any>) =
        orderedQuery(query.whereIn(field, values))

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
     * @return a filtered [OrderedQuery]
     */
    fun whereNotIn(field: String, values: List<Any>) =
        orderedQuery(query.whereNotIn(field, values))

    /**
     * Creates and returns a new Query with the additional filter that documents must contain the
     * specified field, the value must be an array, and that the array must contain the provided
     * value. A [Query] can have only one [whereArrayContains] filter and it cannot be combined
     * with [whereArrayContainsAny].
     *
     * @param field: the field to filter
     * @param values: the values to filter with
     *
     * @return a filtered [OrderedQuery]
     */
    fun whereArrayContains(field: String, value: Any) =
        orderedQuery(query.whereArrayContains(field, value))

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
        orderedQuery(query.whereArrayContainsAny(field, values))

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
        limit: Int = DEFAULT_QUERY_LIMIT,
    ): QueryResult<QuerySnapshot> =
        Query(query).execute(limit)


    fun startAt(values: List<Any?>) =
        orderedQuery(query.startAt(*check(values)))

    fun startAfter(values: List<Any?>) =
        orderedQuery(query.startAfter(*check(values)))

    fun endAt(values: List<Any?>) =
        orderedQuery(query.endAt(*check(values)))

    fun endBefore(values: List<Any?>) =
        orderedQuery(query.endBefore(*check(values)))

    fun startAt(vararg values: Any?) = startAt(values.toList())
    fun startAfter(vararg values: Any?) = startAfter(values.toList())
    fun endAt(vararg values: Any?) = endAt(values.toList())
    fun endBefore(vararg values: Any?) = endBefore(values.toList())

    private fun check(values: List<Any?>): Array<Any?> =
        if (values.size != fields.size)
            throw IllegalArgumentException("Must provide the same number of fields (here ${fields.size}")
        else values.toTypedArray()

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
        OrderedQuery(query.orderBy(field, direction), fields + field)

    /**
     * Return a new [OrderedQuery] where the documents matches a given prefix in a given field.
     *
     * @param field: the field to match
     * @param prefix: the prefix that the value must match
     */
    fun match(prefix: String) = orderedQuery(
        query.startAt(prefix)
            .endAt(prefix + SUFFIX_MATCHER)
    )

}