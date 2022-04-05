package com.github.sdp.ratemyepfl.database

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

sealed class Repository(collectionPath: String) {
    protected val collection = FirebaseFirestore.getInstance()
        .collection(collectionPath)

    companion object {
        const val DEFAULT_LIMIT: Long = 50
    }

    /**
     * Retrieve a given number of items from the collection
     *
     * @param number: the number of element to retrieve
     *
     * @return a QuerySnapshot of the request
     */
    suspend fun take(number: Long): QuerySnapshot {
        return collection.limit(number).get().await()
    }

    /**
     * Retrieve an element by id from the collection
     *
     * @param id: the unique identifier (or key) of the object to retrieve
     */
    suspend fun getById(id: String): DocumentSnapshot {
        return collection.document(id).get().await()
    }
}