package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.google.firebase.firestore.DocumentSnapshot

/**
 * Base class for any item repository. An item is any object that is reviewable.
 *
 * @param collectionPath: Specify the path to the Firebase collection
 *
 * Note: For clean dependency injection, each subclass should provide a binding as
 * done in RepositoryModule. When you want to inject an ItemRepository, you should
 * use ItemRepository<YourClass> instead of YourClass.
 */
sealed class ItemRepositoryImpl<T : Reviewable> constructor(collectionPath: String) :
    ItemRepository<T>, Repository(collectionPath) {

    companion object {
        const val DEFAULT_LIMIT: Long = 50
    }

    /**
     * Define the conversion to apply to DocumentSnapshot
     *
     * @param snapshot: document to convert
     *
     * @return the converted document (as T)
     */
    abstract fun toItem(snapshot: DocumentSnapshot): T?

    override suspend fun getItems(): List<T> {
        return take(DEFAULT_LIMIT)
            .mapNotNull { obj -> toItem(obj) }
    }

    override suspend fun getItemById(id: String): T? = toItem(getById(id))
}
