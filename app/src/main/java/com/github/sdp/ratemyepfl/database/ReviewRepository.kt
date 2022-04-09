package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

class ReviewRepository @Inject constructor(db: FirebaseFirestore) : ReviewRepositoryInterface,
    Repository(db, REVIEW_COLLECTION_PATH) {

    companion object {
        const val REVIEW_COLLECTION_PATH = "reviews"
        const val RATING_FIELD_NAME = "rating"
        const val TITLE_FIELD_NAME = "title"
        const val COMMENT_FIELD_NAME = "comment"
        const val REVIEWABLE_ID_FIELD_NAME = "reviewableId"
        const val DATE_FIELD_NAME = "date"
        const val UID_FIELD_NAME = "uid"
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
                .setUid(getString(UID_FIELD_NAME))
                .setLikers(get(LIKERS_FIELD_NAME) as List<String>)
                .setDislikers(get(DISLIKERS_FIELD_NAME) as List<String>)

            return try {
                builder.build()
            } catch (e: IllegalStateException) {
                null
            }
        }
    }

    /**
     * Add a review to the DB, letting the DB create an unique id
     * @param value : an hashMap of the review
     */
    override fun add(value: HashMap<String, Any?>) {
        collection.document().set(value)
    }

    /**
     * MOSTLY FOR TESTS
     * Add a complete review to the DB
     * @param review : an hashMap of the review
     */
    fun add(review: Review) {
        collection.document(review.id).set(review.toHashMap())
    }

    override suspend fun getReviews(): List<Review> {
        return take(DEFAULT_LIMIT).mapNotNull { obj -> toItem(obj) }
    }

    override suspend fun getReviewById(id: String): Review? = toItem(getById(id))

    override suspend fun getByReviewableId(id: String?): List<Review> {
        return getBy(REVIEWABLE_ID_FIELD_NAME, id.orEmpty())
    }

    private suspend fun getBy(fieldName: String, value: String): List<Review> {
        return collection
            .whereEqualTo(fieldName, value)
            .get()
            .await()
            .mapNotNull { obj -> toItem(obj) }
    }

    private fun toItem(snapshot: DocumentSnapshot): Review? = snapshot.toReview()

    override suspend fun addUidInArray(fieldName: String, id: String, uid: String) {
        val reviewRef = collection.document(id)
        reviewRef.update(fieldName, FieldValue.arrayUnion(uid)).await()
    }

    override suspend fun removeUidInArray(fieldName: String, id: String, uid: String) {
        val reviewRef = collection.document(id)
        reviewRef.update(fieldName, FieldValue.arrayRemove(uid)).await()
    }
}
