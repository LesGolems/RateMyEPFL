package com.github.sdp.ratemyepfl.database.post

import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.RepositoryImpl
import com.github.sdp.ratemyepfl.database.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.database.query.Query.Companion.DEFAULT_QUERY_LIMIT
import com.github.sdp.ratemyepfl.model.review.Subject
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SubjectRepositoryImpl(val repository: RepositoryImpl<Subject>) : SubjectRepository,
    Repository<Subject> by repository {

    @Inject
    constructor(db: FirebaseFirestore) : this(RepositoryImpl<Subject>(db, SUBJECT_COLLECTION_PATH) {
        it.toSubject()
    })

    companion object {
        const val SUBJECT_COLLECTION_PATH = "subjects"
        const val COMMENTS_FIELD_NAME = "comments"
        const val KIND_FIELD_NAME = "kind"

        /**
         * Converts a json data into a Subject
         *
         * @return the subject if the json contains the necessary data, null otherwise
         */
        fun DocumentSnapshot.toSubject(): Subject? = toItem()
    }

    override suspend fun addAndGetId(item: Subject): String {
        val document = repository
            .collection
            .document()

        addWithId(item, document.id).await()
        return document.id
    }

    override fun addWithId(item: Subject, withId: String): Task<String> =
        repository.add(item.withId(withId))


    override suspend fun getSubjects(): List<Subject> =
        repository.take(DEFAULT_QUERY_LIMIT.toLong())

    override suspend fun addComment(subjectId: String, commentId: String) {
        repository.update(subjectId) { subject ->
            subject.copy(comments = subject.comments.plus(commentId))
        }.await()
    }

    override suspend fun removeComment(subjectId: String, commentId: String) {
        repository.update(subjectId) { subject ->
            subject.copy(comments = subject.comments.minus(commentId))
        }
    }


    override suspend fun addUpVote(postId: String, userId: String) {
        repository.update(postId) { subject ->
            if (!subject.likers.contains(userId))
                subject.copy(likers = subject.likers.plus(userId))
            else subject
        }.await()
    }

    override suspend fun removeUpVote(postId: String, userId: String) {
        repository.update(postId) { subject ->
            subject.copy(likers = subject.likers.minus(userId))
        }.await()
    }

    override suspend fun addDownVote(postId: String, userId: String) {
        repository.update(postId) { subject ->
            if (!subject.dislikers.contains(userId)) {
                subject.copy(dislikers = subject.dislikers.plus(userId))
            } else subject
        }.await()
    }

    override suspend fun removeDownVote(postId: String, userId: String) {
        repository.update(postId) { subject ->
            subject.copy(dislikers = subject.dislikers.minus(userId))
        }.await()
    }

}