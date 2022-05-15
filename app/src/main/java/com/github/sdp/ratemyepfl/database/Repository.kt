package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.query.Queryable
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction

interface Repository<T : RepositoryItem> : Queryable {

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
    fun remove(id: String): Task<Void>

    /**
     * Add an item in the database. If the id of the item is null, it auto-generates it
     *
     * @param item: object to add
     */
    fun add(item: T): Task<Void>

    /**
     * Update the the document with the provided [id] by transforming the data.
     * If the document does not exist yet, it fails.
     *
     * @param id: The id of the document to edit
     * @param transform: the transform to apply to the stored data
     *
     * @throws DatabaseException: if the document does not exist.
     *
     * @return the transformed data
     */
    fun update(id: String, transform: (T) -> T): Task<T>

    /**
     * Transform fetched [DocumentSnapshot] into [T]
     *
     * @param document: document to convert
     *
     * @return the converted document, or null if it is not valid
     */
    fun transform(document: DocumentSnapshot): T?
}