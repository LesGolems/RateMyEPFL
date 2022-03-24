package com.github.sdp.ratemyepfl.utils

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class RepositoryUtils {

    companion object {

        suspend fun getLimit(collection: CollectionReference, limit: Long): QuerySnapshot {
            return collection.limit(limit).get().await()
        }

        suspend fun getById(collection: CollectionReference, id: String): DocumentSnapshot {
            return collection.document(id).get().await()

        }
    }
}