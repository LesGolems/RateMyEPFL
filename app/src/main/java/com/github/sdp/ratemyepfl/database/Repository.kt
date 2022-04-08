package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

sealed class Repository(collectionPath: String) {
    protected val db = FirebaseFirestore.getInstance()
    protected val collection = db.collection(collectionPath)

    companion object {
        const val DEFAULT_LIMIT: Long = 50
        const val NUM_REVIEWS_FIELD = "numReviews"
        const val AVERAGE_GRADE_FIELD = "averageGrade"
    }

    /**
     * Retrieve a given number of items from the collection
     *
     * @param number: the number of element to retrieve
     *
     * @return a QuerySnapshot of the request
     */
    suspend fun take(number: Long): QuerySnapshot {
        return collection.limit(number).get().await()
    }

    /**
     * Retrieve an element by id from the collection
     *
     * @param id: the unique identifier (or key) of the object to retrieve
     */
    suspend fun getById(id: String): DocumentSnapshot {
        return collection.document(id).get().await()
    }

    /**
     * Updates the rating of a reviewable item using a transaction for concurrency
     *
     *  @param id : id of the reviewed item
     *  @param rating: rating of the review being added
     */
    protected fun updateRating(id: String, rating: ReviewRating) {
        val docRef = collection.document(id)
        db.runTransaction {
            val snapshot = it.get(docRef)
            val numReviews = snapshot.getString(NUM_REVIEWS_FIELD)?.toInt()
            val averageGrade = snapshot.getString(AVERAGE_GRADE_FIELD)?.toInt()
            if (numReviews != null && averageGrade != null) {
                val newNumReviews = numReviews + 1
                val newAverageGrade = averageGrade + (rating.toValue() - averageGrade) / newNumReviews
                it.update(
                    docRef, "numReviews", newNumReviews.toString(),
                    "averageGrade", newAverageGrade.toString()
                )
            }
        }
    }
}