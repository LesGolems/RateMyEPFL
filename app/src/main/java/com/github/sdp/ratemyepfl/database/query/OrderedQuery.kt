package com.github.sdp.ratemyepfl.database.query

import com.github.sdp.ratemyepfl.database.query.OrderedQuery.OrderedField.Companion.names
import com.github.sdp.ratemyepfl.database.query.OrderedQuery.OrderedField.Companion.orders
import com.github.sdp.ratemyepfl.database.query.Query.Companion.DEFAULT_QUERY_LIMIT
import com.github.sdp.ratemyepfl.database.query.Query.Companion.MAX_QUERY_LIMIT
import com.google.firebase.firestore.Query.Direction.ASCENDING
import com.google.firebase.firestore.Query.Direction.DESCENDING
import com.google.firebase.firestore.QuerySnapshot

typealias OrderDirection = com.google.firebase.firestore.Query.Direction

/**
 * A query ordered on provided fields. The query is order by the order of the fields in
 * [fields].
 */
data class OrderedQuery(private val query: FirebaseQuery, val fields: List<OrderedField>) {

    private fun orderedQuery(query: FirebaseQuery) =
        OrderedQuery(query, fields)

    constructor(query: FirebaseQuery, vararg fields: OrderedField) : this(query, fields.toList())

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
     * @param value: the value to filter with
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
        limit: UInt = DEFAULT_QUERY_LIMIT,
    ): QueryResult<QuerySnapshot> =
        Query(query).execute(limit)

    /**
     * Creates and returns a new [OrderedQuery] that starts at the provided fields relative to the
     * order of the query. The order of the field values must match the order of [names] that
     * define the order of the [OrderedQuery]
     *
     * @param values: a [List] of value for each field on which the query is ordered
     *
     * @throws IllegalArgumentException if [values] does not contain the same number of field as
     * [names]
     *
     * @return an [OrderedQuery] that starts at the given tuple
     */
    fun startAt(values: List<Any?>) =
        orderedQuery(query.startAt(*check(values)))

    /**
     * Creates and returns a new [OrderedQuery] that starts after the provided fields relative to the
     * order of the query. The order of the field values must match the order of [names] that
     * define the order of the [OrderedQuery]
     *
     * @param values: a [List] of value for each field on which the query is ordered
     *
     * @throws IllegalArgumentException if [values] does not contain the same number of field as
     * [names]
     *
     * @return an [OrderedQuery] that starts after the given tuple
     */
    fun startAfter(values: List<Any?>) =
        orderedQuery(query.startAfter(*check(values)))

    /**
     * Creates and returns a new [OrderedQuery] that ends at the provided fields relative to the
     * order of the query. The order of the field values must match the order of [names] that
     * define the order of the [OrderedQuery]
     *
     * @param values: a [List] of value for each field on which the query is ordered
     *
     * @throws IllegalArgumentException if [values] does not contain the same number of field as
     * [names]
     *
     * @return an [OrderedQuery] that ends at the given tuple
     */
    fun endAt(values: List<Any?>) =
        orderedQuery(query.endAt(*check(values)))

    /**
     * Creates and returns a new [OrderedQuery] that ends before the provided fields relative to the
     * order of the query. The order of the field values must match the order of [names] that
     * define the order of the [OrderedQuery]
     *
     * @param values: a [List] of value for each field on which the query is ordered
     *
     * @throws IllegalArgumentException if [values] does not contain the same number of field as
     * [names]
     *
     * @return an [OrderedQuery] that ends before the given tuple
     */
    fun endBefore(values: List<Any?>) =
        orderedQuery(query.endBefore(*check(values)))

    /**
     * Creates and returns a new [OrderedQuery] that starts at the provided fields relative to the
     * order of the query. The order of the field values must match the order of [names] that
     * define the order of the [OrderedQuery]
     *
     * @param values: a [List] of value for each field on which the query is ordered
     *
     * @throws IllegalArgumentException if [values] does not contain the same number of field as
     * [names]
     *
     * @return an [OrderedQuery] that starts at the given tuple
     */
    fun startAt(vararg values: Any?) = startAt(values.toList())

    /**
     * Creates and returns a new [OrderedQuery] that starts after the provided fields relative to the
     * order of the query. The order of the field values must match the order of [names] that
     * define the order of the [OrderedQuery]
     *
     * @param values: a [List] of value for each field on which the query is ordered
     *
     * @throws IllegalArgumentException if [values] does not contain the same number of field as
     * [names]
     *
     * @return an [OrderedQuery] that starts after the given tuple
     */
    fun startAfter(vararg values: Any?) = startAfter(values.toList())

    /**
     * Creates and returns a new [OrderedQuery] that ends at the provided fields relative to the
     * order of the query. The order of the field values must match the order of [names] that
     * define the order of the [OrderedQuery]
     *
     * @param values: a [List] of value for each field on which the query is ordered
     *
     * @throws IllegalArgumentException if [values] does not contain the same number of field as
     * [names]
     *
     * @return an [OrderedQuery] that ends at the given tuple
     */
    fun endAt(vararg values: Any?) = endAt(values.toList())

    /**
     * Creates and returns a new [OrderedQuery] that ends before the provided fields relative to the
     * order of the query. The order of the field values must match the order of [names] that
     * define the order of the [OrderedQuery]
     *
     * @param values: a [List] of value for each field on which the query is ordered
     *
     * @throws IllegalArgumentException if [values] does not contain the same number of field as
     * [names]
     *
     * @return an [OrderedQuery] that ends before the given tuple
     */
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
        direction: com.google.firebase.firestore.Query.Direction = ASCENDING
    ): OrderedQuery =
        if (fields.names().contains(field))
            throw IllegalArgumentException("Cannot order the query twice on the same field ($field)")
        else
            OrderedQuery(query.orderBy(field, direction), fields + OrderedField(field, direction))

    /**
     * Return a new [OrderedQuery] where the documents matches a given prefix in the ordered
     * [names]. The ordered [names] must only contain [String] values.
     *
     * @param prefixes: the prefixes that the fields must match
     *
     * @return an [OrderedQuery] that only contain values matching the prefix
     *
     * @throws IllegalStateException if one of the fields is ordered by [DESCENDING]
     */
    private fun match(prefixes: List<String?>): OrderedQuery {
        if (fields.orders().any { it == OrderDirection.DESCENDING }) {
            throw IllegalStateException("Cannot match on a descending order")
        }
        return startAt(prefixes)
            .endAt(prefixes.map { it?.plus(SUFFIX_MATCHER) })
    }

    /**
     * Return a new [OrderedQuery] where the documents matches a given prefix in the ordered
     * [names]. The ordered [names] must only contain [String] values, and must be [ASCENDING].
     *
     * @param prefixes: the prefixes that the fields must match
     *
     * @return an [OrderedQuery] that only contain values matching the prefix
     */
    fun match(vararg prefixes: String?) = match(prefixes.toList())

    data class OrderedField(val name: String, val order: OrderDirection) {
        companion object {

            /**
             * Returns the names of the [OrderedField]
             *
             * @return a [List] of names
             */
            fun List<OrderedField>.names(): List<String> =
                map { it.name }

            /**
             * Returns the orders of the [OrderedField]
             *
             * @return a [List] of orders
             */
            fun List<OrderedField>.orders(): List<OrderDirection> =
                map { it.order }
        }
    }

}