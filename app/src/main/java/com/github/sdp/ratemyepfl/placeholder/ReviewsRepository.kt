package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.review.CourseReview
import com.google.firebase.firestore.CollectionReference
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ReviewsRepository : Repository<CourseReview>() {
    companion object {
        const val REVIEWS_COLLECTION = "reviews"
    }

    override suspend fun add(value: CourseReview) {
        reviewsCollection().add(Gson().fromJson(value.serialize(), HashMap::class.java))
    }

    override suspend fun get(): Collection<CourseReview> {
        return reviewsCollection().get().await().map { q -> Json.decodeFromString(q.toString()) }
    }

    override suspend fun remove(value: CourseReview) {
        TODO("Not yet implemented")
    }

    private fun reviewsCollection(): CollectionReference {
        return db.collection(REVIEWS_COLLECTION)
    }
}