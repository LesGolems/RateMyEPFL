package com.github.sdp.ratemyepfl.database.query

import org.junit.Assert.*
import org.junit.Test

class QueryStateTest {

    private val success = QueryState.success(1)
    private val loading = QueryState.loading<Int>()
    private val failure = QueryState.failure<Int>("error")

    @Test
    fun mapSuccess() {
        assertEquals(QueryState.success(3), success.map { 3 })
    }

    @Test
    fun mapLoading() {
        assertEquals(true, loading.map { it.toString() } is QueryState.Loading)
    }

    @Test
    fun mapFailure() {
        assertEquals(QueryState.failure<String>("error"), failure.map { it.toString() })
    }
}