package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.database.query.OrderedQuery
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.database.util.Item
import com.github.sdp.ratemyepfl.database.util.Item.Companion.toItem
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
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class LoaderRepositoryImplTest {
    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var repository: LoaderRepositoryImpl<Item>

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val items = (0..30)
        .map { Item(it.toString(), it % 3) }
        .toList()

    private lateinit var query0: OrderedQuery
    private lateinit var query1: OrderedQuery
    private lateinit var query2: OrderedQuery

    private val items0: List<Item> = items.filter {
        it.data == 0
    }

    private val items1 = items.filter { it.data == 1 }

    private val items2 = items.filter { it.data == 2 }


    @Before
    fun setup() {
        hiltRule.inject()
        reset()
    }

    private fun reset() {
        repository = LoaderRepositoryImpl(RepositoryImpl(db, "loaderTest")) {
            it.toItem()
        }
        val query = items.map { repository.add(it) }
        query.forEach { runTest { it.await() } }

        query0 = repository
            .query()
            .whereEqualTo(Item.DATA_FIELD, 0)
            .orderBy(Item.DATA_FIELD)

        query1 = repository
            .query()
            .whereEqualTo(Item.DATA_FIELD, 1)
            .orderBy(Item.DATA_FIELD)

        query2 = repository
            .query()
            .whereEqualTo(Item.DATA_FIELD, 2)
            .orderBy(Item.DATA_FIELD)
    }

    @Test
    fun firstLoadCorrectlyReturnsAskedData() = runTest {
        repository.load(query0, 10000)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception("Should succeed")
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        assertEquals(true, items0.containsAll(it.data))
                        assertEquals(true, it.data.containsAll(items0))
                    }
                }
            }
    }

    @Test
    fun subsequentLoadShouldCacheResult() = runTest { 
        reset()
        repository.load(query0, 3)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception("Should succeed")
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        assertEquals(true, items0.containsAll(it.data))
                        assertEquals(3, it.data.size)
                        repository.load(query0, 3)
                            .collect { l2 ->
                                when (l2) {
                                    is QueryState.Failure -> throw Exception("Should succeed")
                                    is QueryState.Loading -> {}
                                    is QueryState.Success -> {
                                        assertEquals(true, items0.containsAll(it.data))
                                        assertEquals(l2.data, it.data)
                                    }
                                }
                            }
                    }
                }
            }
    }

    private fun clearRepo() = runTest {
        val list = repository
            .repository
            .collection
            .get()
            .await()
            .documents
            .map { it.id }

        list.forEach {
            repository.remove(it).await()
        }
    }
}