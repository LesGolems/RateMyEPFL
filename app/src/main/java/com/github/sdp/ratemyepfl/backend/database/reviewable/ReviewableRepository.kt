package com.github.sdp.ratemyepfl.backend.database.reviewable

import com.github.sdp.ratemyepfl.backend.database.LoaderRepository
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseOrderedQuery
import com.github.sdp.ratemyepfl.backend.database.query.QueryResult
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.google.firebase.FirebaseNetworkException
import java.lang.Integer.min


/**
 * Decorator for Repository that defines reviewable related operations
 */
interface ReviewableRepository<T : Reviewable> : LoaderRepository<T> {

    val offlineData: List<T>

    companion object {
        const val GRADE_FIELD_NAME = "grade"
        const val LIMIT_QUERY_SEARCH = 10u

    }

    /**
     * Search for a matching prefix in a provided field.
     *
     * @param prefix: the prefix to match
     * @param field: the field where the match occurs
     *
     * @return a [QueryResult] containing a [List] of matched values. It matches at most
     * [LIMIT_QUERY_SEARCH] values.
     */
    fun search(
        field: String,
        prefix: String,
        number: UInt = LIMIT_QUERY_SEARCH
    ): QueryResult<List<T>> =
        this.load(
            query().match(field, prefix),
            min(number.toInt(), LIMIT_QUERY_SEARCH.toInt()).toUInt()
        )


    /**
     * Load the data corresponding to the provided [query]. If the loading fails due to a network
     * error, it returns a default value.
     */
    fun loadWithDefault(query: FirebaseOrderedQuery, number: UInt): QueryResult<List<T>> =
        (this as LoaderRepository<T>)
            .load(query, number)
            .withDefault<FirebaseNetworkException>(offlineData)

}