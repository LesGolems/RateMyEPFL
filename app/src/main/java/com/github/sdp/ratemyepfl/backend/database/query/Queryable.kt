package com.github.sdp.ratemyepfl.backend.database.query

interface Queryable {
    /**
     * Returns a new [FirebaseQuery] for the [Queryable]
     */
    fun query(): FirebaseQuery
}