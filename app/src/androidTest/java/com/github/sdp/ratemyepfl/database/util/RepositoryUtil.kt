package com.github.sdp.ratemyepfl.database.util

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
object RepositoryUtil {

    fun clear(collection: CollectionReference) = runTest {
        collection.get().await()
            .mapNotNull { it.id }
            .forEach { collection.document(it).delete().await() }
    }
}