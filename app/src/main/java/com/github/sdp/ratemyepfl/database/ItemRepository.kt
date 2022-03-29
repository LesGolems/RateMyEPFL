package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.ReviewRating

interface ItemRepository<T: Reviewable> {
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

