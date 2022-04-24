package com.github.sdp.ratemyepfl.database


import com.github.sdp.ratemyepfl.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.database.query.Query
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class RepositoryImpl<T : FirestoreItem>(val database: FirebaseFirestore, collectionPath: String) :
    Repository<T> {
    internal val collection = database.collection(collectionPath)

    override suspend fun take(number: Long): QuerySnapshot {
        return collection.limit(number).get().await()
    }

    /**
     * Creates a new query to execute
     *
     * @return a query on the repository [CollectionReference]
     */
    fun query(): Query = Query(collection)


    override suspend fun getById(id: String): DocumentSnapshot =
        collection.document(id).get().await()


    override fun remove(id: String) = collection
        .document(id)
        .delete()


    override fun add(item: T) =
        collection
            .document(item.getId())
            .set(item.toHashMap())

}