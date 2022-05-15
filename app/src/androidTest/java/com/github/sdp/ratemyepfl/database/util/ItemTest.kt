package com.github.sdp.ratemyepfl.database.util

import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.RepositoryImpl
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.database.util.Item.Companion.toItem
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ItemTest {

    @get:Rule
    val hilt = HiltAndroidRule(this)

    @Inject
    lateinit var db: FirebaseFirestore

    lateinit var collection: CollectionReference
    lateinit var repository: Repository<Item>

    @Before
    fun setup() {
        hilt.inject()
        collection = db.collection("itemsTest")
        repository = RepositoryImpl(db, "itemsTest") { it.toItem() }
    }

    @Test
    fun test() = runTest {
        val item = Item("id", 0)
        collection.add(item).await()
        assertEquals(null, collection.get()
            .await()
            .mapNotNull { it.toObject(Item::class.java) })
    }

    @Test
    fun test2() = runTest {
        val item = Item("id", 0)
        val i = repository.query()
            .whereEqualTo(Item.ID_FIELD, "id")
            .execute(1u)
            .mapResult { it.mapNotNull { it.toItem() } }
            .collect {
                when (it) {
                    is QueryState.Failure -> throw Exception()
                    is QueryState.Loading -> {}
                    is QueryState.Success -> assertEquals(it.data.first(), item)
                }
            }

    }
}