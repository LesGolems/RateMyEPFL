package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

class ReviewRepository @Inject constructor() : ReviewRepositoryInterface,
    Repository(REVIEW_COLLECTION_PATH) {

    companion object {
        const val REVIEW_COLLECTION_PATH = "reviews"
        const val RATING_FIELD_NAME = "rating"
        const val TITLE_FIELD_NAME = "title"
        const val COMMENT_FIELD_NAME = "comment"
        const val REVIEWABLE_ID_FIELD_NAME = "reviewableId"
        const val DATE_FIELD_NAME = "date"
        const val LIKERS_FIELD_NAME = "likers"
        const val DISLIKERS_FIELD_NAME = "dislikers"

        /**
         * Converts a json data into a Review
         *
         * @return the review if the json contains the necessary data, null otherwise
         */
        fun DocumentSnapshot.toReview(): Review? {
            val builder = Review.Builder()
                .setId(id)
                .setRating(getString(RATING_FIELD_NAME)?.let { rating -> ReviewRating.valueOf(rating) })
                .setTitle(getString(TITLE_FIELD_NAME))
                .setComment(getString(COMMENT_FIELD_NAME))
                .setReviewableID(getString(REVIEWABLE_ID_FIELD_NAME))
                .setDate(LocalDate.parse(getString(DATE_FIELD_NAME)))
                .setLikers(get(LIKERS_FIELD_NAME) as List<String>)
                .setDislikers(get(DISLIKERS_FIELD_NAME) as List<String>)

            return try {
                builder.build()
            } catch (e: IllegalStateException) {
                null
            }
        }
    }

    override fun add(value: HashMap<String, Any>) {
        collection.document().set(value)
    }

    fun remove(value: Review) {
        collection.document(value.id).delete()
    }

    override suspend fun getReviews(): List<Review> {
        return collection
            .get()
            .await()
            .mapNotNull { obj -> toItem(obj) }
    }

    override suspend fun getByReviewableId(id: String?): List<Review> {
        return getBy(REVIEWABLE_ID_FIELD_NAME, id.orEmpty())
    }

    override suspend fun getReviewById(id: String): Review? = toItem(getById(id))

    suspend fun getByRate(rate: Int): List<Review> {
        return getBy(RATING_FIELD_NAME, rate.toString())
    }

    suspend fun getByDate(date: LocalDate): List<Review> {
        return getBy(DATE_FIELD_NAME, date.toString())
    }

    private suspend fun getBy(fieldName: String, value: String): List<Review> {
        return collection
            .whereEqualTo(fieldName, value)
            .get()
            .await()
            .mapNotNull { obj -> toItem(obj) }
    }

    private fun toItem(snapshot: DocumentSnapshot): Review? = snapshot.toReview()

    override fun addLiker(id: String, uid: String) {
        val reviewRef = collection.document(id)
        reviewRef.update(LIKERS_FIELD_NAME, FieldValue.arrayUnion(uid))
    }

    override fun removeLiker(id: String, uid: String) {
        val reviewRef = collection.document(id)
        reviewRef.update(LIKERS_FIELD_NAME, FieldValue.arrayRemove(uid))
    }

    override fun addDisliker(id: String, uid: String) {
        val reviewRef = collection.document(id)
        reviewRef.update(DISLIKERS_FIELD_NAME, FieldValue.arrayUnion(uid))
    }

    override fun removeDisliker(id: String, uid: String) {
        val reviewRef = collection.document(id)
        reviewRef.update(DISLIKERS_FIELD_NAME, FieldValue.arrayRemove(uid))
    }
}