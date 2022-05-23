package com.github.sdp.ratemyepfl.database.post

import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.RepositoryImpl
import com.github.sdp.ratemyepfl.database.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.model.review.Comment
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
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

    override suspend fun addAndGetId(item: Comment): String {
        val document = repository
            .collection
            .document()

        addWithId(item, document.id).await()
        return document.id
    }

    override fun addWithId(item: Comment, withId: String): Task<String> =
        repository.add(item.withId(withId))


    override suspend fun getBySubjectId(id: String?): List<Comment> =
        getBy(SUBJECT_ID_FIELD_NAME, id.orEmpty())

    private suspend fun getBy(fieldName: String, value: String): List<Comment> {
        return repository
            .collection
            .whereEqualTo(fieldName, value)
            .get()
            .await()
            .mapNotNull { obj -> obj.toComment()?.withId(obj.id) }
    }

    override suspend fun addUpVote(postId: String, userId: String) {
        repository.update(postId) { comment ->
            if (!comment.likers.contains(userId))
                comment.copy(likers = comment.likers.plus(userId))
            else comment
        }.await()
    }

    override suspend fun removeUpVote(postId: String, userId: String) {
        repository.update(postId) { comment ->
            comment.copy(likers = comment.likers.minus(userId))
        }.await()
    }

    override suspend fun addDownVote(postId: String, userId: String) {
        repository.update(postId) { comment ->
            if (!comment.dislikers.contains(userId)) {
                comment.copy(dislikers = comment.dislikers.plus(userId))
            } else comment
        }.await()
    }

    override suspend fun removeDownVote(postId: String, userId: String) {
        repository.update(postId) { comment ->
            comment.copy(dislikers = comment.dislikers.minus(userId))
        }.await()
    }
}