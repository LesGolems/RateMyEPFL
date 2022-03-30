package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Reviewable

interface ItemRepository<T : Reviewable> {
    /**
     * Retrieve the items from the repository
     *
     * @return a list of non-null items
     */
    suspend fun getItems(): List<T>

    /**
     * Retrieve an item from id.
     *
     * @return the item if found, otherwise null
     */
    suspend fun getItemById(id: String): T?
}

