package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.database.query.Query.Companion.DEFAULT_QUERY_LIMIT
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

class ReviewRepository(val repository: RepositoryImpl<Review>) : ReviewRepositoryInterface,
    Repository<Review> by repository {

    @Inject
    constructor(db: FirebaseFirestore) : this(RepositoryImpl<Review>(db, REVIEW_COLLECTION_PATH) {
        it.toReview()
    })

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
                    .withId(id)
            } catch (e: IllegalStateException) {
                null
            }
        }
    }

    /**
     * Add a [Review] with an auto-generated ID. If you want to provide an id, please use the second
     * method.
     *
     * @param item: the [Review] to add
     */
    override fun add(item: Review): Task<Void> {
        val document = repository
            .collection
            .document()

        return addWithId(item, document.id)
    }

    /**
     * Add a [Review] with a provided id. This should be used carefully as it may overwrite data.
     *
     * @param review: [Review] to add
     * @param withId: a provided unique identifier
     *
     */
    fun addWithId(review: Review, withId: String) =
        repository.add(review.withId(withId))


    override suspend fun getReviews(): List<Review> =
        repository.take(DEFAULT_QUERY_LIMIT.toLong())
            .mapNotNull { obj -> obj.toReview()?.withId(obj.id) }


    override suspend fun getReviewById(id: String): Review? = repository
        .getById(id)
        .toReview()
        ?.withId(id)

    override suspend fun getByReviewableId(id: String?): List<Review> {
        return getBy(REVIEWABLE_ID_FIELD_NAME, id.orEmpty())
    }

    private suspend fun getBy(fieldName: String, value: String): List<Review> {
        return repository
            .collection
            .whereEqualTo(fieldName, value)
            .get()
            .await()
            .mapNotNull { obj -> obj.toReview()?.withId(obj.id) }
    }

    override suspend fun addUidInArray(fieldName: String, id: String, uid: String) {
        val reviewRef = repository
            .collection
            .document(id)
        reviewRef.update(fieldName, FieldValue.arrayUnion(uid)).await()
    }

    override suspend fun removeUidInArray(fieldName: String, id: String, uid: String) {
        val reviewRef = repository
            .collection
            .document(id)
        reviewRef.update(fieldName, FieldValue.arrayRemove(uid)).await()
    }
}
