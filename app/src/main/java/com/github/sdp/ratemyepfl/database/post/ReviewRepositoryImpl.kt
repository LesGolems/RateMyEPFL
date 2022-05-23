package com.github.sdp.ratemyepfl.database.post

import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.RepositoryImpl
import com.github.sdp.ratemyepfl.database.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.database.query.Query.Companion.DEFAULT_QUERY_LIMIT
import com.github.sdp.ratemyepfl.model.review.Review
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
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
        const val REVIEWABLE_ID_FIELD_NAME = "reviewableId"

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
    override fun add(item: Review): Task<String> {
        val document = repository
            .collection
            .document()

        return addWithId(item, document.id)
    }

    override suspend fun addAndGetId(item: Review): String {
        val document = repository
            .collection
            .document()

        addWithId(item, document.id).await()
        return document.id
    }

    /**
     * Add a [Review] with a provided id. This should be used carefully as it may overwrite data.
     *
     * @param review: [Review] to add
     * @param withId: a provided unique identifier
     *
     */
    override fun addWithId(item: Review, withId: String): Task<String> =
        repository.add(item.withId(withId))


    override suspend fun getReviews(): List<Review> =
        repository.take(DEFAULT_QUERY_LIMIT.toLong())

    override suspend fun getReviewById(id: String): Review? = repository
        .getById(id)
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

    override suspend fun addUpVote(postId: String, userId: String) {
        repository.update(postId) { review ->
            if (!review.likers.contains(userId))
                review.copy(likers = review.likers.plus(userId))
            else review
        }.await()
    }

    override suspend fun removeUpVote(postId: String, userId: String) {
        repository.update(postId) { review ->
            review.copy(likers = review.likers.minus(userId))
        }.await()
    }

    override suspend fun addDownVote(postId: String, userId: String) {
        repository.update(postId) { review ->
            if (!review.dislikers.contains(userId)) {
                review.copy(dislikers = review.dislikers.plus(userId))
            } else review
        }.await()
    }

    override suspend fun removeDownVote(postId: String, userId: String) {
        repository.update(postId) { review ->
            review.copy(dislikers = review.dislikers.minus(userId))
        }.await()
    }
}