package com.github.sdp.ratemyepfl.backend.database.firebase


import com.github.sdp.ratemyepfl.backend.database.Repository
import com.github.sdp.ratemyepfl.backend.database.RepositoryItem
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.exceptions.DatabaseConversionException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    override fun get(number: Long) = flow {
        val results = collection.limit(number)
            .get()
            .await()
            .mapNotNull(transform)
        emit(results)
    }

    /**
     * Creates a new query to execute
     *
     * @return a query on the repository [CollectionReference]
     */
    override fun query(): FirebaseQuery = FirebaseQuery(collection)


    override fun getById(id: String) = flow {
        val result: T = transform(
            collection.document(id)
                .get()
                .await()
        ) ?: throw NoSuchElementException("No element match the id $id")
        emit(result)
    }


    override fun remove(id: String) = flow {
        val result = collection
            .document(id)
            .delete()
        emit(true)
    }


    override fun add(item: T) = flow {
        val id = item.getId().let { id ->
            collection
                .document(id)
                .set(item)
                .continueWith { id }
                .await()
        }
        emit(id)
    }


    override fun update(id: String, update: (T) -> T): Flow<T> = flow {
        val docRef = collection
            .document(id)
        val result = database
            .runTransaction { transaction ->
                val snapshot = transaction.get(docRef)
                transform(snapshot)?.let { data ->
                    update(data).apply {
                        transaction.set(docRef, this)
                    }
                }
            }.await()
            ?: throw NoSuchElementException("Cannot update an item that does not exist (id: $id)")
        emit(result)
    }

    override fun transform(document: DocumentSnapshot): T? = this.transform.invoke(document)
}