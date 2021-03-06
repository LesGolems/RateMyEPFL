package com.github.sdp.ratemyepfl.backend.database.query

import com.github.sdp.ratemyepfl.backend.database.query.QueryResult.Companion.mapEach
import com.github.sdp.ratemyepfl.backend.database.util.ArrayItem
import com.github.sdp.ratemyepfl.backend.database.util.ArrayItem.Companion.toArrayItem
import com.github.sdp.ratemyepfl.backend.database.util.Item
import com.github.sdp.ratemyepfl.backend.database.util.Item.Companion.toItem
import com.github.sdp.ratemyepfl.backend.database.util.RepositoryUtil
import com.github.sdp.ratemyepfl.exceptions.DatabaseException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class QueryTest {

    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var collection: CollectionReference

    private lateinit var query: FirebaseQuery

    private lateinit var arrayCollection: CollectionReference

    private lateinit var arrayQuery: FirebaseQuery

    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    private val items = (0..10)
        .map { Item(it.toString(), it) }
        .toList()

    private val arrayItems = listOf(
        ArrayItem("id1", listOf(0, 1, 2, 3)),
        ArrayItem("id2", listOf(4, 1, 2, 3)),
        ArrayItem("id3", listOf(5, 1, 2, 3)),
        ArrayItem("id4", listOf(8, 1, 2, 3))
    )

    @Before
    fun setup() {
        hiltAndroidRule.inject()
        collection = db.collection("query_test")
        items.map { collection.document(it.getId()).set(it) }
            .forEach { runTest { it.await() } }
        query = FirebaseQuery(collection)

        arrayCollection = db.collection("query_test_array")
        arrayItems.map { arrayCollection.document().set(it) }
            .forEach { runTest { it.await() } }
        arrayQuery = FirebaseQuery(arrayCollection)
    }

    @After
    fun tearDown() {
        RepositoryUtil.clear(collection)
        RepositoryUtil.clear(arrayCollection)
    }

    @Test
    fun whereEqualTo() {
        query.whereEqualTo(Item.DATA_FIELD, 0)
            .execute()
    }

    @Test
    fun whereGreaterThan() {
        checkQuery(
            query.whereGreaterThan(Item.DATA_FIELD, 9),
            listOf(items.last())
        )
    }

    @Test
    fun whereGreaterThanOrEqualTo() {
        checkQuery(query.whereGreaterThanOrEqualTo(Item.DATA_FIELD, 9),
            items.filter { it.data >= 9 })
    }

    @Test
    fun whereLessThan() {
        checkQuery(query.whereLessThan(Item.DATA_FIELD, 3),
            items.filter { it.data < 3 })
    }

    @Test
    fun whereLessThanOrEqualTo() {
        checkQuery(query.whereLessThanOrEqualTo(Item.DATA_FIELD, 3),
            items.filter { it.data <= 3 })
    }

    @Test
    fun whereNotEqualTo() {
        checkQuery(query.whereNotEqualTo(Item.DATA_FIELD, 10),
            items.filter { it.data != 10 })
    }

    @Test
    fun whereIn() {
        val required = (0..3).toList()
        checkQuery(query.whereIn(Item.DATA_FIELD, required),
            items.filter { it.data in required })
    }

    @Test
    fun whereNotIn() {
        val required = (0..3).toList()
        checkQuery(query.whereNotIn(Item.DATA_FIELD, required),
            items.filter { it.data !in required })
    }

    @Test
    fun whereArrayContains() = runTest {
        arrayQuery.whereArrayContains(ArrayItem.DATA_FIELD, 3)
            .execute(10u)
            .mapResult { it.mapNotNull { it.toArrayItem().toString() } }
            .collect {
                if (it is QueryState.Success) {
                    assertEquals(4, it.data.size)
                }
            }
    }

    @Test
    fun whereArrayContainsAny() = runTest {
        val required = listOf(5, 8)
        arrayQuery.whereArrayContainsAny(ArrayItem.DATA_FIELD, required)
            .execute(10u)
            .mapResult { it.mapNotNull { it.toArrayItem() } }
            .collect {
                if (it is QueryState.Success) {
                    assertEquals(2, it.data.size)
                }
            }
    }

    @Test
    fun execute() = runTest {
        query.execute()
            .collect {
                if (it is QueryState.Success) {
                    assertEquals(FirebaseQuery.DEFAULT_QUERY_LIMIT.toInt(), it.data.size())
                }
            }

        query.execute(10000u)
            .collect {
                if (it is QueryState.Success) {
                    assertEquals(true, it.data.size() <= FirebaseQuery.MAX_QUERY_LIMIT.toInt())
                }
            }
    }

    @Test
    fun orderBy() {
        checkQuery(query.orderBy(Item.DATA_FIELD),
            items.sortedBy { it.data })
    }

    @Test
    fun orderByReverse() = runTest {
        query.orderBy(
            Item.DATA_FIELD,
            com.google.firebase.firestore.Query.Direction.DESCENDING
        ).execute(1000u)
            .mapResult { it.mapNotNull { it.toItem() } }
            .collect {
                if (it is QueryState.Success) {
                    assertEquals(items.sortedBy { -it.data }, it.data)
                }
            }

    }

    @Test
    fun match() = runTest {
        query.match(Item.ID_FIELD, "1")
            .execute()
            .mapResult { it.documents.filterNotNull() }
            .mapEach { it.toItem() }
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception()
                    is QueryState.Loading -> {}
                    is QueryState.Success ->
                        assertEquals(listOf(Item("1", 1), Item("10", 10)), it.data)
                }
            }
        query.match(Item.ID_FIELD, "nothing")
            .execute()
            .mapResult { it.documents.filterNotNull() }
            .mapEach { it.toItem() }
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception()
                    is QueryState.Loading -> {}
                    is QueryState.Success ->
                        assertEquals(listOf<Item>(), it.data)
                }
            }


    }

    private fun checkQuery(query: FirebaseQuery, expected: List<Item>) = runTest {
        query.execute(items.size.toUInt())
            .mapResult {
                it.mapNotNull {
                    try {
                        it.toItem()
                    } catch (e: Exception) {
                        null
                    }
                }
            }
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception()
                    is QueryState.Loading -> {}
                    is QueryState.Success -> assertEquals(expected, it.data)
                }
            }
    }

    private fun checkQuery(query: FirebaseOrderedQuery, expected: List<Item>) = runTest {
        query.execute(items.size.toUInt())
            .mapResult {
                it.mapNotNull {
                    try {
                        it.toItem()
                    } catch (e: Exception) {
                        null
                    }
                }
            }
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception()
                    is QueryState.Loading -> {}
                    is QueryState.Success -> assertEquals(expected, it.data)
                }
            }
    }

    @Test
    fun executeFailingCodeReturnsDatabaseException() = runTest {
        query.whereEqualTo("someNonExistingField", "someNonValidValid")
            .execute()
            .collect {
                when (it) {
                    is QueryState.Failure -> assertEquals(true, it.error is DatabaseException)
                    else -> {}
                }
            }
    }
}