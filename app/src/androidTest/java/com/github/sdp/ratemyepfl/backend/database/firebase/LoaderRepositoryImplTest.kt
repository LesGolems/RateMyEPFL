package com.github.sdp.ratemyepfl.backend.database.firebase

import com.github.sdp.ratemyepfl.backend.database.query.FirebaseOrderedQuery
import com.github.sdp.ratemyepfl.backend.database.query.QueryState
import com.github.sdp.ratemyepfl.backend.database.util.Item
import com.github.sdp.ratemyepfl.backend.database.util.Item.Companion.toItem
import com.github.sdp.ratemyepfl.backend.database.util.RepositoryUtil
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class LoaderRepositoryImplTest {
    @Inject
    lateinit var db: FirebaseFirestore

    private val collectionPath = "loaderTest"

    private lateinit var repository: LoaderRepositoryImpl<Item>

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val items = (0..30)
        .map { Item(it.toString(), it) }
        .toList()

    private lateinit var query0: FirebaseOrderedQuery
    private lateinit var query1: FirebaseOrderedQuery
    private lateinit var query2: FirebaseOrderedQuery

    private val items0: List<Item> = items.filter {
        it.data in 0..9
    }

    @Before
    fun setup() {
        hiltRule.inject()
        reset()
    }

    @After
    fun teardown() {
        clearRepo()
    }

    private fun reset() {
        repository = LoaderRepositoryImpl(RepositoryImpl(db, collectionPath) {
            it.toItem()
        })
        val query = items.map { repository.add(it) }
        query.forEach { runTest { it.collect() } }
        query0 = repository
            .query()
            .whereLessThan(Item.DATA_FIELD, 10)
            .orderBy(Item.DATA_FIELD)
            .orderBy(Item.ID_FIELD)

        query1 = repository
            .query()
            .whereGreaterThanOrEqualTo(Item.DATA_FIELD, 10)
            .whereLessThan(Item.DATA_FIELD, 20)
            .orderBy(Item.DATA_FIELD)

        query2 = repository
            .query()
            .whereGreaterThanOrEqualTo(Item.DATA_FIELD, 20)
            .orderBy(Item.DATA_FIELD)
    }

    @Test
    fun firstLoadCorrectlyReturnsAskedData() = runTest {
        repository.load(query0, 10000u)
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
        repository.load(query0, 3u)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception("Should succeed")
                    is QueryState.Loading -> {}
                    is QueryState.Success -> {
                        assertEquals(true, items0.containsAll(it.data))
                        assertEquals(3, it.data.size)
                        repository.load(query0, 3u)
                            .collect { l2 ->
                                when (l2) {
                                    is QueryState.Failure -> throw Exception("Should succeed")
                                    is QueryState.Loading -> {}
                                    is QueryState.Success -> {
                                        assertEquals(true, items0.containsAll(it.data))
                                        assertEquals(6, l2.data.size)
                                    }
                                }
                            }
                    }
                }
            }
    }

    private fun clearRepo() = runTest {
        RepositoryUtil.clear(db.collection(collectionPath))
    }

    @Test
    fun loadedWorksAccordingly() = runTest {
        val query = repository.query().orderBy(Item.DATA_FIELD)
            .whereGreaterThanOrEqualTo(Item.DATA_FIELD, 10)
        var i = listOf<Item>()
        repository.load(query).collect {
            if (it is QueryState.Success) {
                i = it.data
            }
        }

        assertEquals(
            i, repository.loaded(
                repository.query().orderBy(Item.DATA_FIELD)
                    .whereGreaterThanOrEqualTo(Item.DATA_FIELD, 10)
            )
        )
    }
}