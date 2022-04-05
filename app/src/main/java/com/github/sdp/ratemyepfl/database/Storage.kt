package com.github.sdp.ratemyepfl.database

/**
 * An interface representing an item collection, in which items may have a maximum size.
 * This interface is mostly for implementing adapters of Firebase Storage.
 */
interface Storage<T> {

    val MAX_ITEM_SIZE: Long

    suspend fun get(id: String) : T?

    suspend fun put(item: T)

    suspend fun remove(item: T)
}