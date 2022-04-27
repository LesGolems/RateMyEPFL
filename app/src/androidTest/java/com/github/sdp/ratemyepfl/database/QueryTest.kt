package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.database.query.Query
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.database.util.Item
import com.github.sdp.ratemyepfl.database.util.Item.Companion.DATA_FIELD
import com.github.sdp.ratemyepfl.database.util.Item.Companion.ID_FIELD
import com.github.sdp.ratemyepfl.database.util.Item.Companion.toItem
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class QueryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var repository: RepositoryImpl<Item>


    @Before
    fun setup() {
        hiltRule.inject()
        repository = RepositoryImpl<Item>(db, "repositoryTest")
        runTest { initialItems.forEach { repository.add(it) } }
    }

    private val initialItems = (0..11)
        .map { Item(it.toString(), it) }
        .toList()

    @Test
    fun executesSuccessfullyReturnsTheResult() = runTest {
        val l = 4u
        repository.query()
            .execute(l)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception("Test should succeed")
                    is QueryState.Loading -> {}
                    is QueryState.Success -> assertEquals(l.toInt(), it.data.size())
                }
            }

    }

    @Test
    fun test() = runTest {
        var l: List<Item> = listOf()
        var s: DocumentSnapshot? = null
        repository.query()
            .orderBy(Item.DATA_FIELD)
            .execute(3u)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception("Test should succeed")
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        l = it.data.mapNotNull { snapshot -> snapshot.toItem() }
                        assertEquals(3, l.size)
                        s = it.data.documents.last()
                    }
                }
            }
        repository.query()
            .orderBy(Item.DATA_FIELD)
            .startAfter(s?.get(Item.DATA_FIELD))
            .execute(3u)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception("Test should succeed")
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        assertEquals(3, it.data.size())
                        l =
                            l + it.data.mapNotNull { snapshot -> snapshot.toItem() }
                    }
                }
            }
        assertEquals(6, l.toSet().size)
    }

    @Test
    fun executeReturnsAFailureWhenAnErrorOccurs() = runTest {
        val mockQuery = Mockito.mock(FirebaseQuery::class.java)
        Mockito.`when`(mockQuery.get()).thenThrow(RuntimeException::class.java)

        Query(mockQuery).execute()
            .collect {
                when (it) {
                    is QueryState.Success -> throw Exception("Test should fail")
                    else -> {}
                }
            }
    }

    @Test
    fun executeCannotExceedMaxLimit() {
        val size = 100
        (0..size).toList()
            .map { Item(it.toString(), it) }
            .forEach { runTest { repository.add(it).await() } }

        runTest {
            repository.query()
                .execute(size.toUInt())
                .collect {
                    when (it) {
                        is QueryState.Failure -> throw Exception("Test should succeed")
                        is QueryState.Loading -> {}
                        is QueryState.Success -> assertEquals(Query.MAX_QUERY_LIMIT.toInt(), it.data.size())
                    }
                }

        }
    }
}