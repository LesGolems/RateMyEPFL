package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.util.Item
import com.github.sdp.ratemyepfl.database.util.Item.Companion.toItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
class RepositoryImplTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: FirebaseFirestore

    private lateinit var repository: RepositoryImpl<Item>


    @Before
    fun setup() {
        hiltRule.inject()
        repository = RepositoryImpl<Item>(db, "repositoryTest")
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
            repository.add(item).await()
            val fetched = repository.getById(id)
                .toItem()
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
            initialItems.forEach {
                repository.add(item).await()
            }
            repository.add(item).await()
            repository.add(item).await()
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
    fun removeCorrectlyExistingItem() {
        val id = "privateID"
        val data = -1
        val item = Item(id, data)
        runTest {
            repository.add(item).await()
            repository.remove(item.getId()).await()

            assertEquals(null, repository.getById(item.getId()).toItem())
        }
    }

    @Test
    fun removeNonexistentItemDoesNotModifyTheContent() = runTest {
        clearRepo()
        initialItems.forEach {
            repository.add(it)
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


    private fun clearRepo() = runTest {
        val list = repository.collection
            .get()
            .await()
            .documents
            .map { it.id }

        list.forEach {
            repository.remove(it).await()
        }
    }
}