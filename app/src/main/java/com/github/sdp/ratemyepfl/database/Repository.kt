package com.github.sdp.ratemyepfl.database

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

open class Repository {
    protected val db = FirebaseFirestore.getInstance()

    val a = FirebaseStorage.getInstance("gs://ratemyepfl.appspot.com")

    suspend fun getLimit(collection: CollectionReference, limit: Long): QuerySnapshot {
        return collection.limit(limit).get().await()
    }

    suspend fun getById(collection: CollectionReference, id: String): DocumentSnapshot {
        return collection.document(id).get().await()
    }
}