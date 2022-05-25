package com.github.sdp.ratemyepfl.backend.database.firebase

import com.github.sdp.ratemyepfl.backend.database.query.QueryState
import com.github.sdp.ratemyepfl.backend.database.util.Item
import com.github.sdp.ratemyepfl.backend.database.util.Item.Companion.toItem
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class RepositoryImplTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var repository: RepositoryImpl<Item>


    @Before
    fun setup() {
        hiltRule.inject()
        repository = RepositoryImpl(db, "repositoryTest") { it.toItem() }
    }

    @After
    fun teardown() {
        clearRepo()
    }

    private val initialItems = (0..10)
        .map { Item(it.toString(), it) }
        .toList()

    @Test
    fun addWorksForAnyItem() {
        val id = "privateID"
        val data = -1
        val item = Item(id, data)
        runTest {
            repository.add(item).collect()
            val fetched = repository.getById(id).last()
            assertEquals(item, fetched)
        }
        clearRepo()
    }

    @Test
    fun addTwiceTheSameItemOverwriteIt() {
        val id = "privateID"
        val data = -1
        val item = Item(id, data)
        clearRepo()
        runTest {
            initialItems.forEach { _ ->
                repository.add(item).collect()
            }
            repository.add(item).collect()
            repository.add(item).collect()
            val size = repository
                .collection
                .get()
                .await()
                .size()
            assertEquals(1, size)
        }
        clearRepo()
    }

    @Test
    fun removeCorrectlyExistingItem() = runTest {
        val id = "privateID"
        val data = -1
        val item = Item(id, data)
        runTest {
            repository.add(item).collect()
            repository.remove(item.getId()).collect()

            assertEquals(null, repository.getById(item.getId()).last())
        }
    }

    @Test
    fun removeNonexistentItemDoesNotModifyTheContent() = runTest {
        clearRepo()
        initialItems.forEach {
            repository.add(it).collect()
        }

        repository.remove("Some wrong id")

        assertEquals(
            initialItems.size, repository
                .collection
                .get()
                .await()
                .size()
        )
    }

    @Test
    fun queryReturnsAnExecutableQuery() = runTest {
        repository.add(initialItems[0]).collect()
        repository.query()
            .execute(1u)
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception()
                    is QueryState.Loading -> {}
                    is QueryState.Success -> assertEquals(1, it.data.size())
                }
            }
    }

    private fun clearRepo() = runTest {
        val list = repository.collection
            .get()
            .await()
            .documents
            .map { it.id }

        list.forEach {
            repository.remove(it).collect()
        }
    }

    @Test
    fun updateTest() = runTest {
        clearRepo()
        val item = Item("0", 0)
        repository.add(item).collect()
        repository.update(item.getId()) {
            it.copy(data = 1)
        }.collect()

        val x = repository.getById(item.getId()).last()

        assertEquals(1, x.data)
    }

    @Test
    fun t() = runTest {
        assertEquals(null, repository.update("someRandomId") {
            it
        }.last())
    }
}