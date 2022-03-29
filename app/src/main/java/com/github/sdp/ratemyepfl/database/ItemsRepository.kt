package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

sealed class ItemsRepository<T : Reviewable> @Inject constructor(collectionPath: String) :
    ItemsRepositoryInterface<T>, Repository(collectionPath) {

    companion object {
        const val DEFAULT_LIMIT: Long = 50
    }

    abstract fun toItem(snapshot: DocumentSnapshot): T?

    override suspend fun getItems(): List<T> {
        return take(DEFAULT_LIMIT)
            .mapNotNull { obj -> toItem(obj) }
    }

    // Function to get for a generic Reviewable
    override suspend fun getItemById(id: String): T? = toItem(getById(id))
}
