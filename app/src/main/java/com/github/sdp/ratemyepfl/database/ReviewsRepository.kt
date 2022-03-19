package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.Review.Companion.toReview
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

class ReviewsRepository @Inject constructor() : ReviewsRepositoryInterface, Repository(){

    companion object {
        const val COLLECTION = "reviews"
    }

    override fun add(value: Review) {
        reviewsCollection().document(value.title)
                           .set(value.toHashMap())
    }

    override suspend fun get(): List<Review?> {
        return reviewsCollection().get().await().map {
                q -> q.toReview()
        }
    }

    override suspend fun getByReviewableId(id: String?): List<Review?>{
        return getBy("reviewableId", id.orEmpty())
    }

    suspend fun getByRate(rate: Int): List<Review?> {
        return getBy("rate", rate.toString())
    }

    suspend fun getByDate(date: LocalDate): List<Review?> {
        return getBy("date", date.toString())
    }

    fun remove(value: Review) {
        reviewsCollection().document(value.title).delete()
    }

    private fun reviewsCollection(): CollectionReference {
        return db.collection(COLLECTION)
    }

    private suspend fun getBy(name: String, value: String): List<Review?> {
        return reviewsCollection()
                    .whereEqualTo(name, value)
                    .get()
                    .await()
                    .map { q -> q.toReview() }
    }
}