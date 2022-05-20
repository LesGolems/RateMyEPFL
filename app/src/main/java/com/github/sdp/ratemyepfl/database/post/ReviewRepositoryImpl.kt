package com.github.sdp.ratemyepfl.database.post

import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.RepositoryImpl
import com.github.sdp.ratemyepfl.database.post.PostRepository.Companion.COMMENT_FIELD_NAME
import com.github.sdp.ratemyepfl.database.post.PostRepository.Companion.DATE_FIELD_NAME
import com.github.sdp.ratemyepfl.database.post.PostRepository.Companion.DISLIKERS_FIELD_NAME
import com.github.sdp.ratemyepfl.database.post.PostRepository.Companion.LIKERS_FIELD_NAME
import com.github.sdp.ratemyepfl.database.post.PostRepository.Companion.TITLE_FIELD_NAME
import com.github.sdp.ratemyepfl.database.post.PostRepository.Companion.UID_FIELD_NAME
import com.github.sdp.ratemyepfl.database.query.Query.Companion.DEFAULT_QUERY_LIMIT
import com.github.sdp.ratemyepfl.exceptions.DatabaseException
import com.github.sdp.ratemyepfl.model.review.Post
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
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
        fun DocumentSnapshot.toReview(): Review? = try {
            val builder = Review.Builder()
                .setReviewableID(getString(REVIEWABLE_ID_FIELD_NAME))
                .setRating(getString(RATING_FIELD_NAME)?.let { rating -> ReviewRating.valueOf(rating) })
                .setTitle(getString(TITLE_FIELD_NAME))
                .setComment(getString(COMMENT_FIELD_NAME))
                .setDate(LocalDate.parse(getString(DATE_FIELD_NAME)))
                .setUid(getString(UID_FIELD_NAME))
                .setLikers(get(LIKERS_FIELD_NAME) as List<String>)
                .setDislikers(get(DISLIKERS_FIELD_NAME) as List<String>)

            builder.build()
                .withId(id)
        } catch (e: IllegalStateException) {
            null
        } catch (e: Exception) {
            throw DatabaseException("Failed to retrieve and convert the review (from $e \n ${e.stackTrace})")
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
    override fun addWithId(item: Review, withId: String): Task<Void> =
        repository.add(item.withId(withId))


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