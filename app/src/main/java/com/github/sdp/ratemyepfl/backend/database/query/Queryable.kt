package com.github.sdp.ratemyepfl.backend.database.query

interface Queryable {
    /**
     * Returns a new [Query] for the [Queryable]
     */
    fun query(): Query
}