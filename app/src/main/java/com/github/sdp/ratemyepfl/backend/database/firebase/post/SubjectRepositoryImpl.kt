package com.github.sdp.ratemyepfl.backend.database.firebase.post

import com.github.sdp.ratemyepfl.backend.database.Repository
import com.github.sdp.ratemyepfl.backend.database.firebase.RepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.firebase.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.backend.database.post.SubjectRepository
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseQuery.Companion.DEFAULT_QUERY_LIMIT
import com.github.sdp.ratemyepfl.model.review.Subject
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
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

        /**
         * Converts a json data into a [Subject]
         *
         * @return the subject if the json contains the necessary data, null otherwise
         */
        fun DocumentSnapshot.toSubject(): Subject? = toItem()
    }

    /**
     * Add a [Subject] with an auto-generated ID. If you want to provide an id, please use the second
     * method.
     *
     * @param item: the [Subject] to add
     */
    override fun add(item: Subject): Flow<String> {
        val document = repository
            .collection
            .document()

        return addWithId(item, document.id)
    }

    override fun addWithId(item: Subject, withId: String) =
        repository.add(item.withId(withId))

    override suspend fun getSubjects(): List<Subject> =
        repository.collection
            .limit(DEFAULT_QUERY_LIMIT.toLong())
            .get()
            .await()
            .mapNotNull { obj ->
                obj.toSubject()?.withId(obj.id)
            }

    override suspend fun addComment(subjectId: String, commentId: String) {
        repository.update(subjectId) { subject ->
            subject.copy(comments = subject.comments.plus(commentId))
        }.collect()
    }

    override suspend fun removeComment(subjectId: String, commentId: String) {
        repository.update(subjectId) { subject ->
            subject.copy(comments = subject.comments.minus(commentId))
        }.collect()
    }

    override suspend fun addUpVote(postId: String, userId: String) {
        repository.update(postId) { subject ->
            if (!subject.likers.contains(userId))
                subject.copy(likers = subject.likers.plus(userId))
            else subject
        }.collect()
    }

    override suspend fun removeUpVote(postId: String, userId: String) {
        repository.update(postId) { subject ->
            subject.copy(likers = subject.likers.minus(userId))
        }.collect()
    }

    override suspend fun addDownVote(postId: String, userId: String) {
        repository.update(postId) { subject ->
            if (!subject.dislikers.contains(userId)) {
                subject.copy(dislikers = subject.dislikers.plus(userId))
            } else subject
        }.collect()
    }

    override suspend fun removeDownVote(postId: String, userId: String) {
        repository.update(postId) { subject ->
            subject.copy(dislikers = subject.dislikers.minus(userId))
        }.collect()
    }
}