package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.backend.database.query.QueryResult
import com.github.sdp.ratemyepfl.backend.database.query.QueryResult.Companion.asQueryResult
import com.github.sdp.ratemyepfl.backend.database.query.QueryResult.Companion.flatten
import com.github.sdp.ratemyepfl.backend.database.query.QueryResult.Companion.mapEach
import com.github.sdp.ratemyepfl.backend.database.query.QueryState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QueryResultTest {

    @Test
    fun mapResultWorksForTrivialType() = runTest {
        val qr = QueryResult {
            0
        }

        val mapped = qr.mapResult { it.toString() }

        mapped.collect {
            assertEquals("0", (it as QueryState.Success).data)
        }
    }

    @Test
    fun mapEachWorksForTrivialMapping() = runTest {
        val qr = flow<QueryState<List<Int>>> {
            val ls = listOf(1, 2, 3)
            this.emit(QueryState.success(ls))
        }.asQueryResult()

        val mapped = qr.mapEach { 10 }
            .mapResult { it.reduce { x, y -> x + y } }

        mapped.collect {
            assertEquals(30, (it as QueryState.Success).data)
        }
    }

    @Test
    fun flattenReturnsTheFirstError() = runTest {
        val r1 = QueryResult {
            0
        }

        val r2 = QueryResult<Int> {
            throw Exception("1")
        }

        val r3 = QueryResult<Int> {
            throw Exception("2")
        }

        val results = listOf(r1, r2, r3)
        results.flatten()
            .collect {
                when (it) {
                    is QueryState.Failure -> assertEquals("1", it.error.message)
                    is QueryState.Loading -> {}
                    is QueryState.Success -> throw Exception("Should succeed")
                }
            }
    }

    @Test
    fun flattenResultTheListOfResultInOrder() {
        @Test
        fun flattenReturnsTheFirstError() = runTest {
            val r1 = QueryResult {
                0
            }

            val r2 = QueryResult<Int> {
                1
            }

            val r3 = QueryResult<Int> {
                2
            }

            val results = listOf(r1, r2, r3)
            results.flatten()
                .collect {
                    when (it) {
                        is QueryState.Failure -> throw Exception("Should succeed")
                        is QueryState.Loading -> {}
                        is QueryState.Success -> assertEquals(listOf(1, 2, 3), it.data)
                    }
                }
        }

        @Test
        fun flattenReturnsOnlyOneLoadingInTheBeginning() {
            @Test
            fun flattenReturnsTheFirstError() = runTest {
                val r1 = QueryResult {
                    0
                }

                val r2 = QueryResult<Int> {
                    1
                }

                val r3 = QueryResult<Int> {
                    2
                }

                val results = listOf(r1, r2, r3)
                var loading = false
                results.flatten()
                    .collect {
                        loading = when (it) {
                            is QueryState.Loading -> if (!loading) {
                                true
                            } else throw Exception("Should only load once")
                            else -> true
                        }
                    }
            }
        }

    }
}