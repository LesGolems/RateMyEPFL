package com.github.sdp.ratemyepfl.backend.database.query

import com.github.sdp.ratemyepfl.backend.database.query.QueryState
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryStateTest {

    private val success = QueryState.success(1)
    private val loading = QueryState.loading<Int>()
    private val failure = QueryState.failure<Int>(Exception("error"))

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
        assertEquals(failure, failure.map { it.toString() })
    }

    @Test
    fun mapErrorForFailure() {
        val error = RuntimeException("failed")
        val runtime = QueryState.failure<Int>(error)
        assertEquals(runtime, failure.mapError { error })
    }

    @Test
    fun mapErrorForFailurePreservesTheType() {
        val error = RuntimeException("failed")
        assertEquals(true, (failure.mapError { error }) is QueryState.Failure<Int>)
    }

    @Test
    fun mapErrorForFailurePreservesTheState() {
        val error = RuntimeException("failed")
        assertEquals(success, success.mapError { error })
        assertEquals(loading, loading.mapError { error })
    }
}