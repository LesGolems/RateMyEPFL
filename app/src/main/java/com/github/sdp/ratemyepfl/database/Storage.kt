package com.github.sdp.ratemyepfl.database

interface Storage<T> {

    val MAX_ITEM_SIZE: Long

    suspend fun get(id: String) : T?

    suspend fun put(item: T)

    suspend fun remove(item: T)
}