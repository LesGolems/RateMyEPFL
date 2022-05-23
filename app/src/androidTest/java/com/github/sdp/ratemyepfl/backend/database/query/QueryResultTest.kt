package com.github.sdp.ratemyepfl.backend.database.query

import com.github.sdp.ratemyepfl.backend.database.query.QueryResult.Companion.asQueryResult
import com.github.sdp.ratemyepfl.backend.database.query.QueryResult.Companion.mapEach
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
        QueryResult.success(listOf(1, 2, 3))

    private val success = QueryResult.success(1)
    private val error = Exception("Failed")
    private val failure = QueryResult.failure<Int>(error)

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
                    is QueryState.Failure<String> -> assertEquals(error, it.error)
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

    @Test
    fun mapErrorIsConsistent() = runTest {
        val error = RuntimeException()
        failure
            .mapError { error }
            .collect {
                when (it) {
                    is QueryState.Failure -> assertEquals(error, it.error)
                    else -> throw Exception("Should not be $it")
                }
            }
    }

    @Test
    fun builderConstructorEmitsTheDefaultLoadingInTheBeginning() = runTest {
        var x = 0
        var loading = false
        QueryResult {
            emit(QueryState.success(0))
        }.collect {
            when (it) {
                is QueryState.Failure -> x = 2
                is QueryState.Loading -> {
                    loading = true
                    assertEquals(0, x)
                }
                is QueryState.Success -> x = 1
            }
        }

        assertEquals(true, loading)
    }

    @Test
    fun builderConstructorEmitsTheFailureIfSomethingFails() = runTest {
        val x = 0
        val error = Exception()
        QueryResult<Int> {
            throw error
        }.collect {
            when (it) {
                is QueryState.Failure -> assertEquals(error, it.error)
                is QueryState.Loading -> assertEquals(0, x)
                is QueryState.Success -> throw Exception("Should never succeed")
            }
        }

    }

    @Test
    fun withDefaultProvidesADefaultValueForTheRightException() {
        val failure = flow {
            emit(QueryState.failure<Int>(FakeException()))
        }.asQueryResult()
            .withDefault<FakeException>(0)

        runTest {
            failure.collect {
                when (it) {
                    is QueryState.Failure -> throw Exception("Should be success")
                    is QueryState.Loading -> throw Exception("Should be success")
                    is QueryState.Success -> assertEquals(0, it.data)
                }
            }
        }
    }

    @Test
    fun withDefaultThrowsForTheWrongException() {
        val failure = flow {
            emit(QueryState.failure<Int>(Exception()))
        }.asQueryResult()
            .withDefault<FakeException>(0)

        runTest {
            failure.collect {
                when (it) {
                    is QueryState.Failure -> assertEquals(true, it.error is Exception)
                    is QueryState.Loading -> throw Exception("Should be failure")
                    is QueryState.Success -> throw Exception("Should be failure")
                }
            }
        }
    }

    private class FakeException : Exception()
}