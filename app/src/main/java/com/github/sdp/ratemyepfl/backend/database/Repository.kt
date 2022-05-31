package com.github.sdp.ratemyepfl.backend.database

import com.github.sdp.ratemyepfl.backend.database.query.Queryable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface Repository<T : RepositoryItem> : Queryable {

    companion object {
        const val DEFAULT_NUMBER_ITEMS = 10L
    }
    /**
     * Retrieve a given number of items from the collection
     *
     * @param number: the number of element to retrieve
     *
     * @return a list of results
     */
    fun get(number: Long = DEFAULT_NUMBER_ITEMS): Flow<List<T>>

    /**
     * Retrieve an element by id from the collection
     *
     * @param id: the unique identifier (or key) of the object to retrieve
     *
     * @return the first element matching the provided id
     *
     * @throws NoSuchElementException if the element was not found
     */
    fun getById(id: String): Flow<T>

    /**
     * @param id : the identifier of the item to delete
     */
    fun remove(id: String): Flow<Boolean>

    /**
     * Add an item in the database. If the id of the item is null, it auto-generates it
     *
     * @param item: object to add
     *
     * @return the id of the element added
     */
    fun add(item: T): Flow<String>

    /**
     * Update the the document with the provided [id] by transforming the data.
     * If the document does not exist yet, it fails and returns null.
     *
     * @param id: The id of the document to edit
     * @param update: the transform to apply to the stored data
     *
     *
     * @return the transformed data, or null if the document does not exist.
     */
    fun update(id: String, update: (T) -> T): Flow<T>

    /**
     * Transform fetched [DocumentSnapshot] into [T]
     *
     * @param document: document to convert
     *
     * @return the converted document, or null if it is not valid
     */
    fun transform(document: DocumentSnapshot): T?
}