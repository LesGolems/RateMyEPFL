package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.review.Review

interface ReviewsRepository {
    fun add(value: Review)
    suspend fun get(): List<Review>
    suspend fun getByReviewableId(id: String?): List<Review>
}