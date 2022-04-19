package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.QueryResult
import com.github.sdp.ratemyepfl.model.items.Reviewable

/**
 * Defines [Reviewable] specific loading methods. The generic type [T] is used to determine the
 * returned type.
 */
interface ReviewableRepository<T: Reviewable> {
    /**
     * Load a given number of [Reviewable] by decreasing number of reviews.
     *
     * @param number: number of item to load
     *
     * @return a [QueryResult] containing the result as a list of reviewable
     */
    fun loadMostRated(number: Long): QueryResult<List<T>>

    /**
     * Load a given number of [Reviewable] by decreasing average grade.
     *
     * @param number: number of item to load
     *
     * @return a [QueryResult] containing the result as a list of reviewable
     */
    fun loadBestRated(number: Long): QueryResult<List<T>>
}