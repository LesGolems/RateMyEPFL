package com.github.sdp.ratemyepfl.database.query

import com.github.sdp.ratemyepfl.database.query.QueryResult.Companion.mapEach
import com.github.sdp.ratemyepfl.database.util.ArrayItem
import com.github.sdp.ratemyepfl.database.util.ArrayItem.Companion.toArrayItem
import com.github.sdp.ratemyepfl.database.util.Item
import com.github.sdp.ratemyepfl.database.util.Item.Companion.ID_FIELD
import com.github.sdp.ratemyepfl.database.util.Item.Companion.toItem
import com.github.sdp.ratemyepfl.database.util.RepositoryUtil
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.IllegalStateException
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class OrderedQueryTest {
    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var collection: CollectionReference

    private lateinit var arrayCollection: CollectionReference

    private lateinit var arrayQuery: OrderedQuery

    private val arrayItems = listOf(
        ArrayItem("id1", listOf(0, 1, 2, 3)),
        ArrayItem("id2", listOf(4, 1, 2, 3)),
        ArrayItem("id3", listOf(5, 1, 2, 3)),
        ArrayItem("id4", listOf(8, 1, 2, 3))
    )



    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    val items = (0..10)
        .map { Item(it.toString(), it) }
        .toList()

    @Before
    fun setup() {
        hiltAndroidRule.inject()
        this.collection = db.collection("ordered_query_test")
        items.map { collection.document(it.getId()).set(it) }
            .forEach { runTest { it.await() } }
        query = Query(collection)
            .orderBy(Item.DATA_FIELD)

        arrayCollection = db.collection("query_test_array")
        arrayItems.map { arrayCollection.document().set(it.toHashMap()) }
            .forEach { runTest { it.await() } }
        arrayQuery = Query(arrayCollection)
            .orderBy(ArrayItem.DATA_FIELD)

    }

    private lateinit var query: OrderedQuery

    @After
    fun teardown() = runTest {
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
            .mapResult { it.mapNotNull { it.toArrayItem() } }
            .collect {
                if (it is QueryState.Success) {
                    assertEquals(4, it.data.size)
                }
            }
    }

    @Test
    fun whereArrayContainsAny() = runTest {
        val required = listOf(5, 8)
        arrayQuery
            .whereArrayContainsAny(ArrayItem.DATA_FIELD, required)
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
                    assertEquals(Query.DEFAULT_QUERY_LIMIT.toInt(), it.data.size())
                }
            }

        query.execute(10000u)
            .collect {
                if (it is QueryState.Success) {
                    assertEquals(true, it.data.size() <= Query.MAX_QUERY_LIMIT.toInt())
                }
            }
    }

    @Test
    fun orderBy() {
        assertThrows(IllegalArgumentException::class.java) {
            query.orderBy(Item.DATA_FIELD)
        }
        checkQuery(query.orderBy(Item.ID_FIELD),
            items.sortedBy { it.data })
    }

    @Test
    fun orderByReverse() = runTest {
        query.orderBy(
            Item.ID_FIELD,
            com.google.firebase.firestore.Query.Direction.DESCENDING
        ).execute(1000u)
            .mapResult { it.mapNotNull { it.toItem() } }
            .collect {
                if (it is QueryState.Success) {
                    assertEquals(items.sortedBy { it.data }, it.data)
                }
            }

    }

    @Test
    fun match() = runTest {
        Query(collection)
            .orderBy(ID_FIELD)
            .match("1")
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

        Query(collection)
            .orderBy(ID_FIELD)
            .match("nothing")
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

        assertThrows(IllegalStateException::class.java) {
            Query(collection)
                .orderBy(ID_FIELD, OrderDirection.DESCENDING)
                .match("nothing")
        }

    }
    @Test
    fun startAt() {
        assertThrows(IllegalArgumentException::class.java) {
            query.orderBy(ID_FIELD)
                .startAt("garbage")
        }

        checkQuery(query
            .startAt(listOf(3)),
        items.filter { it.data >= 3 })
    }

    @Test
    fun startAfter() {
        assertThrows(IllegalArgumentException::class.java) {
            query.orderBy(ID_FIELD)
                .startAfter("garbage")
        }

        checkQuery(query
            .startAfter(listOf(3)),
            items.filter { it.data > 3 })
    }

    @Test
    fun endAt() {
        assertThrows(IllegalArgumentException::class.java) {
            query.orderBy(ID_FIELD)
                .endAt("garbage")
        }

        checkQuery(query
            .endAt(listOf(3)),
            items.filter { it.data <= 3 })
    }

    @Test
    fun endBefore() {
        assertThrows(IllegalArgumentException::class.java) {
            query.orderBy(ID_FIELD)
                .endBefore("garbage")
        }

        checkQuery(query
            .endBefore(listOf(3)),
            items.filter { it.data < 3 })
    }

    private fun checkQuery(query: OrderedQuery, expected: List<Item>) = runTest {
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
}