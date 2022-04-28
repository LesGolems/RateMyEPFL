package com.github.sdp.ratemyepfl.database


import com.github.sdp.ratemyepfl.database.query.Query
import com.github.sdp.ratemyepfl.database.query.Queryable
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await

class RepositoryImpl<T : RepositoryItem>(
    val database: FirebaseFirestore,
    collectionPath: String,
    /** Transform a [DocumentSnapshot] into [T], and returns null if it fails **/
    private val transform: (DocumentSnapshot) -> T?
) :
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

    override fun update(id: String, transform: (T) -> T): Task<Transaction> {
        val docRef = collection
            .document(id)
        return database
            .runTransaction { transaction ->
                val snapshot = transaction.get(docRef)
                this.transform(snapshot)?.let { data ->
                    transaction.update(docRef, transform(data).toHashMap())
                }
            }
    }

    override fun transform(document: DocumentSnapshot): T? = this.transform.invoke(document)
}
