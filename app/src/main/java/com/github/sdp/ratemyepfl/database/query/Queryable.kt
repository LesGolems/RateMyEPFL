package com.github.sdp.ratemyepfl.database.query

interface Queryable {
    /**
     * Returns a new [Query] for the [Queryable]
     */
    fun query(): Query
}