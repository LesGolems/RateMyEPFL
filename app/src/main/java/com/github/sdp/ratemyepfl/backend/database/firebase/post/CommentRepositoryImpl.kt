package com.github.sdp.ratemyepfl.backend.database.firebase.post

import com.github.sdp.ratemyepfl.backend.database.Repository
import com.github.sdp.ratemyepfl.backend.database.firebase.RepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.backend.database.firebase.post.CommentRepositoryImpl.Companion.toComment
import com.github.sdp.ratemyepfl.model.review.Comment
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommentRepositoryImpl(val repository: RepositoryImpl<Comment>) : CommentRepository,
    Repository<Comment> by repository {

    @Inject
    constructor(db: FirebaseFirestore) : this(RepositoryImpl<Comment>(db, COMMENT_COLLECTION_PATH) {
        it.toComment()
    })

    companion object {
        const val COMMENT_COLLECTION_PATH = "comments"
        const val SUBJECT_ID_FIELD_NAME = "subjectId"

        /**
         * Converts a json data into a Comment
         *
         * @return the comment if the json contains the necessary data, null otherwise
         */
        fun DocumentSnapshot.toComment(): Comment? = toItem()
    }

    /**
     * Add a [Comment] with an auto-generated ID. If you want to provide an id, please use the second
     * method.
     *
     * @param item: the [Comment] to add
     */
    override fun add(item: Comment): Flow<String> {
        val document = repository
            .collection
            .document()

        return addWithId(item, document.id)
    }

    override fun addWithId(item: Comment, withId: String) =
        repository.add(item.withId(withId))

    override fun getBySubjectId(id: String) = flow {
        val results = repository
            .collection
            .whereEqualTo(SUBJECT_ID_FIELD_NAME, id)
            .get()
            .await()
            .mapNotNull { obj -> obj.toComment()?.withId(obj.id) }
        emit(results)
    }

    override suspend fun addUpVote(postId: String, userId: String) {
        repository.update(postId) { comment ->
            if (!comment.likers.contains(userId))
                comment.copy(likers = comment.likers.plus(userId))
            else comment
        }.collect()
    }

    override suspend fun removeUpVote(postId: String, userId: String) {
        repository.update(postId) { comment ->
            comment.copy(likers = comment.likers.minus(userId))
        }.collect()
    }

    override suspend fun addDownVote(postId: String, userId: String) {
        repository.update(postId) { comment ->
            if (!comment.dislikers.contains(userId)) {
                comment.copy(dislikers = comment.dislikers.plus(userId))
            } else comment
        }.collect()
    }

    override suspend fun removeDownVote(postId: String, userId: String) {
        repository.update(postId) { comment ->
            comment.copy(dislikers = comment.dislikers.minus(userId))
        }.collect()
    }
}