package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.backend.database.query.QueryResult
import com.github.sdp.ratemyepfl.backend.database.query.QueryResult.Companion.asQueryResult
import com.github.sdp.ratemyepfl.backend.database.query.QueryResult.Companion.mapEach
import com.github.sdp.ratemyepfl.backend.database.query.QueryState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class QueryResultTest {

    @Test
    fun mapResultWorksForTrivialType() = runTest {
        val qr = QueryResult {
            0
        }

        val mapped = qr.mapResult { it.toString() }

        assertEquals("0", (mapped.last() as QueryState.Success).data)
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

}