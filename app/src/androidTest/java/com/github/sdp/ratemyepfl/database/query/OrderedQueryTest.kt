package com.github.sdp.ratemyepfl.database.query

import com.github.sdp.ratemyepfl.database.query.QueryResult.Companion.mapEach
import com.github.sdp.ratemyepfl.database.util.Item
import com.github.sdp.ratemyepfl.database.util.Item.Companion.toItem
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
class OrderedQueryTest {
    @Inject
    lateinit var db: FirebaseFirestore

    lateinit var collection: CollectionReference

    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    val items = (0..10)
        .map { Item(it.toString(), it) }
        .toList()

    @Before
    fun setup() {
        hiltAndroidRule.inject()
        collection = db.collection("ordered_query_test")
        items.map { collection.document(it.getId()).set(it) }
            .forEach { runTest { it.await() } }

    }

    private fun query(): Query = Query(collection)

    @Test
    fun matchTest() = runTest {
        query().match(Item.ID_FIELD, "0")
            .execute()
            .mapResult { it.documents }
            .mapEach { it.toItem()!! }
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception()
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        assertEquals(items[0], it.data.first())
                        assertEquals(it.data.size, 1)
                    }
                }
            }
    }

    @After
    fun teardown() = runTest {
        collection.get().await()
            .mapNotNull { it.toItem()?.getId() }
            .forEach { collection.document(it).delete().await() }
    }
}