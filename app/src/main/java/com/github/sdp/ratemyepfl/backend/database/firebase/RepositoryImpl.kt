package com.github.sdp.ratemyepfl.backend.database.firebase


import com.github.sdp.ratemyepfl.backend.database.Repository
import com.github.sdp.ratemyepfl.backend.database.RepositoryItem
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.exceptions.DatabaseConversionException
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RepositoryImpl<T : RepositoryItem>(
    private val database: FirebaseFirestore,
    collectionPath: String,
    /** Transform a [DocumentSnapshot] into [T], and returns null if it fails **/
    private val transform: (DocumentSnapshot) -> T?
) :
    Repository<T> {
    val collection = database.collection(collectionPath)

    companion object {
        inline fun <reified T : RepositoryItem> DocumentSnapshot.toItem(): T? = try {
            toObject(T::class.java)
        } catch (e: Exception) {
            throw DatabaseConversionException(T::class.java, e.message ?: "")
        }
    }

    override suspend fun take(number: Long) =
        collection.limit(number)
            .get()
            .await()
            .mapNotNull(transform)

    /**
     * Creates a new query to execute
     *
     * @return a query on the repository [CollectionReference]
     */
    override fun query(): FirebaseQuery = FirebaseQuery(collection)


    override suspend fun getById(id: String) =
        transform(
            collection.document(id)
                .get()
                .await()
        )


    override fun remove(id: String) = collection
        .document(id)
        .delete()


    override fun add(item: T): Task<String> = item.getId().let { id ->
        collection
            .document(id)
            .set(item)
            .continueWith { id }
    }


    override fun update(id: String, transform: (T) -> T): Task<T> {
        val docRef = collection
            .document(id)
        return database
            .runTransaction { transaction ->
                val snapshot = transaction.get(docRef)
                this.transform(snapshot)?.let { data ->
                    transform(data).apply {
                        transaction.set(docRef, this)
                    }
                }
            }
    }

    override fun transform(document: DocumentSnapshot): T? = this.transform.invoke(document)
}