package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.Review.Companion.toReview
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

class ReviewsRepositoryImpl @Inject constructor() : ReviewsRepository, Repository(COLLECTION_PATH) {

    companion object {
        const val COLLECTION_PATH = "reviews"

        // Fake photo ids
        private val FAKE_PHOTOS = listOf(
            R.drawable.room3,
            R.drawable.room1,
            R.drawable.room4,
            R.drawable.room2,
            R.drawable.room5,
            R.drawable.room6
        )
    }

    override fun add(value: Review) {
        collection.document()
            .set(value.toHashMap())
    }

    override suspend fun get(): List<Review> {
        return collection.get().await().mapNotNull { q ->
            q.toReview()
        }
    }

    override suspend fun getByReviewableId(id: String?): List<Review> {
        return getBy("reviewableId", id.orEmpty())
    }

    // Not linked to Firebase Storage yet
    override suspend fun getPhotosByReviewableId(id: String?): List<Int> {
        return FAKE_PHOTOS
    }

    suspend fun getByRate(rate: Int): List<Review> {
        return getBy("rate", rate.toString())
    }

    suspend fun getByDate(date: LocalDate): List<Review> {
        return getBy("date", date.toString())
    }

    fun remove(value: Review) {
        collection.document(value.title).delete()
    }


    private suspend fun getBy(name: String, value: String): List<Review> {
        return collection
            .whereEqualTo(name, value)
            .get()
            .await()
            .mapNotNull { q -> q.toReview() }
    }
}