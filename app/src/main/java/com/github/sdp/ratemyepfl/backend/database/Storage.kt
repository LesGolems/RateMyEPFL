package com.github.sdp.ratemyepfl.backend.database

/**
 * An interface representing an item collection, in which items may have a maximum size.
 * This interface is mostly for implementing adapters of Firebase Storage.
 */
interface Storage<T> {

    /**
     * Maximum supported size (in bytes) for an image by Firebase Storage
     */
    val MAX_ITEM_SIZE: Long

    /**
     * Returns the item for the given [id].
     * Returns null in case of errors.
     */
    suspend fun get(id: String): T?

    /**
     * Adds [item] to the collection.
     * Throws an IllegalArgumentException if the item is too big for the collection.
     */
    suspend fun add(item: T)

    /**
     * Removes item with [id] from the collection.
     */
    suspend fun remove(id: String)

    /**
     * Returns all the items in the sub-directory [dir].
     */
    suspend fun getByDirectory(dir: String): List<T>

    /**
     * Adds [item] to the collection in the sub-directory [dir].
     * Throws an IllegalArgumentException if the item is too big for the collection.
     */
    suspend fun addInDirectory(item: T, dir: String)


    /**
     * Removes item with [id] from the collection in the sub-directory [dir].
     */
    suspend fun removeInDirectory(id: String, dir: String)
}