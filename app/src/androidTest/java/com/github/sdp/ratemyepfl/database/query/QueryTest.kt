package com.github.sdp.ratemyepfl.database.query

import com.github.sdp.ratemyepfl.database.query.QueryResult.Companion.mapEach
import com.github.sdp.ratemyepfl.database.util.Item
import com.github.sdp.ratemyepfl.database.util.Item.Companion.toItem
import com.github.sdp.ratemyepfl.database.util.RepositoryUtil
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
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

    private lateinit var query: Query

    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    private val items = (0..10)
        .map { Item(it.toString(), it) }
        .toList()

    @Before
    fun setup() {
        hiltAndroidRule.inject()
        collection = db.collection("query_test")
        items.map { collection.document(it.getId()).set(it) }
            .forEach { runTest { it.await() } }
        query = Query(collection)
    }

    @After
    fun tearDown() {
        RepositoryUtil.clear(collection)
    }

    @Test
    fun whereEqualTo() {
        query.whereEqualTo(Item.DATA_FIELD, 0)
            .execute()
    }

    @Test
    fun whereGreaterThan() {
    }

    @Test
    fun whereGreaterThanOrEqualTo() {
    }

    @Test
    fun whereLessThan() {
    }

    @Test
    fun whereLessThanOrEqualTo() {
    }

    @Test
    fun whereNotEqualTo() {
    }

    @Test
    fun whereIn() {
    }

    @Test
    fun whereNotIn() {
    }

    @Test
    fun whereArrayContains() {
    }

    @Test
    fun whereArrayContainsAny() {
    }

    @Test
    fun execute() {
    }

    @Test
    fun orderBy() {
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
}