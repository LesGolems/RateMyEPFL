package com.github.sdp.ratemyepfl.database.query

import com.github.sdp.ratemyepfl.database.query.QueryResult.Companion.asQueryResult
import com.github.sdp.ratemyepfl.database.query.QueryResult.Companion.mapEach
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class QueryResultTest {
    private val loading =
        flow<QueryState<Int>> {
            emit(QueryState.loading())
        }.asQueryResult()

    private val list =
        QueryResult.success(listOf<Int>(1, 2, 3))

    private val success = QueryResult.success(1)
    private val failure = QueryResult.failure<Int>(Exception("Failed"))

    @Test
    fun mapResultWorksForLoading() = runTest {
        loading.mapResult { it.toString() }
            .collect {
                when (it) {
                    is QueryState.Loading -> {}
                    else -> throw Exception("Should be loading")
                }
            }
    }

    @Test
    fun mapResultWorksForFailure() = runTest {
        failure.mapResult { it.toString() }
            .collect {
                when (it) {
                    is QueryState.Failure<String> -> assertEquals(failure, it.error)
                    else -> throw Exception("Should be failure")
                }
            }
    }

    @Test
    fun mapResultWorksForSuccess() = runTest {
        success.mapResult { it.toString() }
            .collect {
                when (it) {
                    is QueryState.Success -> assertEquals("1", it.data)
                    else -> throw Exception("Should be success")
                }
            }
    }

    @Test
    fun mapEachIsConsistent() = runTest {
        list.mapEach {
            it * 2
        }.collect {
            when (it) {
                is QueryState.Success -> assertEquals(listOf(2, 4, 6), it.data)
                else -> throw Exception("")
            }
        }
    }
}