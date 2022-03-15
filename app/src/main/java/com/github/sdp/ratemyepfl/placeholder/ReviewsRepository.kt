package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.review.Review
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ReviewsRepository : Repository<Review>() {
    companion object {
        const val REVIEWS_COLLECTION = "reviews"
    }

    override suspend fun add(value: Review) {
        collectionReference().add(Json.encodeToString(value))
    }

    override suspend fun get(): Collection<Review> {
        return collectionReference().get().await().map { q -> Json.decodeFromString(q.toString()) }
    }

    override suspend fun remove(value: Review) {
        TODO("Not yet implemented")
    }

    private fun collectionReference(): CollectionReference {
        return db.collection(REVIEWS_COLLECTION)
    }
}