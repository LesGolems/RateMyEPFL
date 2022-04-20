package com.github.sdp.ratemyepfl.database

import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class RepositoryImplTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    class Item(private val id: String, val data: Int) : FirestoreItem {
        override fun getId(): String = id

        override fun toHashMap(): HashMap<String, Any?> =
            hashMapOf("data" to data)
    }

    private val repository = RepositoryImpl<Item>(FirebaseFirestore.getInstance(), "repositoryTest")

    private val initialItems = (0..10)
        .map { Item(it.toString(), it) }
        .toList()

    @Test
    fun addWorksForAnyItem() {
        val id = "privateID"
        val data = -1
        val item = Item("id", data)
        runTest {
            repository.addAsync(item)
        }
    }

}