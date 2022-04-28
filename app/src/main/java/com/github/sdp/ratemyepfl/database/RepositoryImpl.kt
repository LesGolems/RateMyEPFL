package com.github.sdp.ratemyepfl.database


import com.github.sdp.ratemyepfl.database.query.Query
import com.github.sdp.ratemyepfl.database.query.Queryable
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class RepositoryImpl<T : RepositoryItem>(val database: FirebaseFirestore, collectionPath: String) :
    Repository<T>, Queryable {
    internal val collection = database.collection(collectionPath)

    override suspend fun take(number: Long): QuerySnapshot {
        return collection.limit(number).get().await()
    }

    /**
     * Creates a new query to execute
     *
     * @return a query on the repository [CollectionReference]
     */
    override fun query(): Query = Query(collection)


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
//
//package com.github.sdp.ratemyepfl.database
//
//
//import com.github.sdp.ratemyepfl.database.query.Query
//import com.github.sdp.ratemyepfl.database.query.Queryable
//import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl
//import com.google.android.gms.tasks.Task
//import com.google.firebase.firestore.*
//import com.google.firebase.firestore.ktx.getField
//import kotlinx.coroutines.tasks.await
//
//class RepositoryImpl<T : RepositoryItem>(
//    val database: FirebaseFirestore,
//    collectionPath: String,
//    val transform: (DocumentSnapshot) -> T
//) :
//    Repository<T>, Queryable {
//    internal val collection = database.collection(collectionPath)
//
//    override suspend fun take(number: Long): QuerySnapshot {
//        return collection.limit(number).get().await()
//    }
//
//    /**
//     * Creates a new query to execute
//     *
//     * @return a query on the repository [CollectionReference]
//     */
//    override fun query(): Query = Query(collection)
//
//
//    override suspend fun getById(id: String): DocumentSnapshot =
//        collection.document(id).get().await()
//
//
//    override fun remove(id: String) = collection
//        .document(id)
//        .delete()
//
//
//    override fun add(item: T) =
//        collection
//            .document(item.getId())
//            .set(item.toHashMap())
//
//    override fun update(id: String, field: String, transform: (T) -> T): Task<Transaction> {
//        val docRef = collection
//            .document(id)
//        return database
//            .runTransaction {
//                val snapshot = it.get(docRef)
//                val data = this.transform(snapshot)
//                it.update(docRef, transform(data).toHashMap())
//            }
//    }
//
//}