package com.github.sdp.ratemyepfl.viewmodel.filter

import com.github.sdp.ratemyepfl.backend.database.query.FirebaseOrderedQuery
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.model.items.Reviewable

/**
 * Define a filtering schema for a query to the database.
 *
 * ***NB: Any query ordered on several fields requires an index.***
 */
sealed interface ReviewableFilter<T : Reviewable> {

    /**
     * Convert a filter to an executable [FirebaseOrderedQuery].
     */
    fun toQuery(initialQuery: FirebaseQuery): FirebaseOrderedQuery

    /**
     * Determine whether this filter can be composed with the provided filter.
     *
     * @return true if there is a conflict
     */
    infix fun conflictWith(filter: ReviewableFilter<T>): Boolean

}