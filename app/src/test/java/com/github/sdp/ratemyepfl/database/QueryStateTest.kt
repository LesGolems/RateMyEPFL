package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.backend.database.query.QueryResult
import com.github.sdp.ratemyepfl.backend.database.query.QueryState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QueryStateTest {
    @Test
    fun mapWorksForLoading() {
        val state = QueryState.Loading<Int>()
        assertEquals(true, state.map { it.toString() } is QueryState.Loading)
    }

    @Test
    fun mapWorksForSuccess() {
        val state = QueryState.Success(0)
        assertEquals("0", (state.map { it.toString() } as QueryState.Success).data)
    }

    @Test
    fun mapWorksForFailure() {
        val error = Exception("Failed")
        val state = QueryState.Failure<Int>(error)
        assertEquals(error, (state.map { it.toString() } as QueryState.Failure).error)
    }

    @Test
    fun test() = runTest {
        var x = 0
        QueryResult {
            3
        }.mapResult {
            x = 3
            it
        }.collect()
        assertEquals(3, x)
    }

    @Test
    fun mapPreservesTheState() {
        val data = 32
        val error = Exception("error")

        val state1 = QueryState.Loading<Int>()
        val state2 = QueryState.success(data)
        val state3 = QueryState.failure<Int>(error)

        assertEquals(1, computeForState(state1.map { it.toString() }))
        assertEquals(2, computeForState(state2.map { it.toString() }))
        assertEquals(0, computeForState(state3.map { it.toString() }))

    }

    private fun <T> computeForState(state: QueryState<T>): Int = when (state) {
        is QueryState.Failure -> 0
        is QueryState.Loading -> 1
        is QueryState.Success -> 2
    }

    @Test
    fun flatMapPreservesError() {
        val x = QueryState.failure<Int>(Exception())
        assertEquals(true, x.flatMap { QueryState.loading<Int>() } is QueryState.Failure)
    }
}