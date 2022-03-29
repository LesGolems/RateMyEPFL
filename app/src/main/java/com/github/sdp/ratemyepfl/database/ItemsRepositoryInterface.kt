package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.model.review.ReviewRating

sealed interface ItemsRepositoryInterface<T: Reviewable> {
    suspend fun getItems(): List<T>
    suspend fun getItemById(id: String): T?
}

