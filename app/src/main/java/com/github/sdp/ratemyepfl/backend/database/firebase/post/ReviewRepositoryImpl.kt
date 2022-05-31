package com.github.sdp.ratemyepfl.backend.database.firebase.post

import com.github.sdp.ratemyepfl.backend.database.Repository
import com.github.sdp.ratemyepfl.backend.database.firebase.RepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.backend.database.post.ReviewRepository
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
        const val REVIEWABLE_ID_FIELD_NAME = "reviewableId"

        /**
         * Converts a json data into a [Review]
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

    /**
     * Add a [Review] with a provided id. This should be used carefully as it may overwrite data.
     *
     * @param item: [Review] to add
     * @param withId: a provided unique identifier
     *
     */
    override fun addWithId(item: Review, withId: String) =
        repository.add(item.withId(withId))


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

    override suspend fun addUpVote(postId: String, userId: String) {
        repository.update(postId) { review ->
            if (!review.likers.contains(userId))
                review.copy(likers = review.likers.plus(userId))
            else review
        }.collect()
    }

    override suspend fun removeUpVote(postId: String, userId: String) {
        repository.update(postId) { review ->
            review.copy(likers = review.likers.minus(userId))
        }.collect()
    }

    override suspend fun addDownVote(postId: String, userId: String) {
        repository.update(postId) { review ->
            if (!review.dislikers.contains(userId)) {
                review.copy(dislikers = review.dislikers.plus(userId))
            } else review
        }.collect()
    }

    override suspend fun removeDownVote(postId: String, userId: String) {
        repository.update(postId) { review ->
            review.copy(dislikers = review.dislikers.minus(userId))
        }.collect()
    }
}