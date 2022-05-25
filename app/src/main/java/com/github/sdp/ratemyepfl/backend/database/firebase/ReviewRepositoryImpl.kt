package com.github.sdp.ratemyepfl.backend.database.firebase

import com.github.sdp.ratemyepfl.backend.database.Repository
import com.github.sdp.ratemyepfl.backend.database.ReviewRepository
import com.github.sdp.ratemyepfl.backend.database.firebase.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseQuery.Companion.DEFAULT_QUERY_LIMIT
import com.github.sdp.ratemyepfl.model.review.Review
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewRepositoryImpl(val repository: RepositoryImpl<Review>) : ReviewRepository,
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
        fun DocumentSnapshot.toReview(): Review? = toItem()
    }

    /**
     * Add a [Review] with an auto-generated ID. If you want to provide an id, please use the second
     * method.
     *
     * @param item: the [Review] to add
     */
    override fun add(item: Review): Flow<String> {
        val document = repository
            .collection
            .document()

        return addWithId(item, document.id)
    }

    override suspend fun addAndGetId(item: Review): String {
        val document = repository
            .collection
            .document()

        addWithId(item, document.id).collect()
        return document.id
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
        repository.get(DEFAULT_QUERY_LIMIT.toLong()).last()

    override suspend fun getReviewById(id: String): Review? = repository
        .getById(id)
        .lastOrNull()
        ?.withId(id)

    override fun getByReviewableId(id: String): Flow<List<Review>> = flow {
        val result = repository
            .collection
            .whereEqualTo(REVIEWABLE_ID_FIELD_NAME, id)
            .get()
            .await()
            .mapNotNull { obj -> obj.toReview()?.withId(obj.id) }
        emit(result)
    }

    override suspend fun addUpVote(reviewId: String, userId: String) {
        repository.update(reviewId) { review ->
            if (!review.likers.contains(userId))
                review.copy(likers = review.likers.plus(userId))
            else review
        }.collect()
    }

    override suspend fun removeUpVote(reviewId: String, userId: String) {
        repository.update(reviewId) { review ->
            review.copy(likers = review.likers.minus(userId))
        }.collect()
    }

    override suspend fun addDownVote(reviewId: String, userId: String) {
        repository.update(reviewId) { review ->
            if (!review.dislikers.contains(userId)) {
                review.copy(dislikers = review.dislikers.plus(userId))
            } else review
        }.collect()
    }

    override suspend fun removeDownVote(reviewId: String, userId: String) {
        repository.update(reviewId) { review ->
            review.copy(dislikers = review.dislikers.minus(userId))
        }.collect()
    }
}