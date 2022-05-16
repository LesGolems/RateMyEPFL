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

    private lateinit var collection: CollectionReference
    private lateinit var _repository: Repository<Item>
    var repository: Repository<Item>
        get() = _repository
        set(value) {
            _repository = value
        }

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
    }

    @Test
    fun test2() = runTest {
        val item = Item("id", 0)
        repository.query()
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