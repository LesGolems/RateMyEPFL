package com.github.sdp.ratemyepfl.database

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Deferred

interface Repository<T: FirestoreItem> {
    companion object {
        const val DEFAULT_QUERY_LIMIT: Long = 10
        const val MAX_QUERY_LIMIT: Long = 50
    }

    /**
     * Retrieve a given number of items from the collection
     *
     * @param number: the number of element to retrieve
     *
     * @return a QuerySnapshot of the request
     */
    suspend fun take(number: Long): QuerySnapshot

    /**
     * Retrieve an element by id from the collection
     *
     * @param id: the unique identifier (or key) of the object to retrieve
     */
    suspend fun getById(id: String): DocumentSnapshot


    /**
     * @param id : the identifier of the item to delete
     */
    fun removeAsync(id: String): Deferred<Void>

    /**
     * Add an item in the database. If the id of the item is null, it auto-generates it
     *
     * @param item: object to add
     */
    fun addAsync(item: T): Deferred<Void>
}