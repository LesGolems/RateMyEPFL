package com.github.sdp.ratemyepfl.database.post

import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.RepositoryImpl
import com.github.sdp.ratemyepfl.exceptions.DatabaseException
import com.github.sdp.ratemyepfl.model.review.Subject
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

class SubjectRepositoryImpl(val repository: RepositoryImpl<Subject>) : SubjectRepository,
    Repository<Subject> by repository {

    @Inject
    constructor(db: FirebaseFirestore) : this(RepositoryImpl<Subject>(db, SUBJECT_COLLECTION_PATH) {
        it.toSubject()
    })

    companion object {
        const val SUBJECT_COLLECTION_PATH = "subjects"
        const val COMMENTATORS_FIELD_NAME = "commentators"

        /**
         * Converts a json data into a Review
         *
         * @return the review if the json contains the necessary data, null otherwise
         */
        fun DocumentSnapshot.toSubject(): Subject? = try {
            val builder = Subject.Builder()
                .setCommentators(getString(COMMENTATORS_FIELD_NAME) as List<String>)
                .setTitle(getString(PostRepository.TITLE_FIELD_NAME))
                .setComment(getString(PostRepository.COMMENT_FIELD_NAME))
                .setDate(LocalDate.parse(getString(PostRepository.DATE_FIELD_NAME)))
                .setUid(getString(PostRepository.UID_FIELD_NAME))
                .setLikers(get(PostRepository.LIKERS_FIELD_NAME) as List<String>)
                .setDislikers(get(PostRepository.DISLIKERS_FIELD_NAME) as List<String>)

            builder.build()
                .withId(id)
        } catch (e: IllegalStateException) {
            null
        } catch (e: Exception) {
            throw DatabaseException("Failed to retrieve and convert the subject (from $e \n ${e.stackTrace})")
        }
    }

    override suspend fun getSubjects(): List<Subject> =
        listOf(
            Subject(
                "TITLE",
                "comment",
                LocalDate.now(),
                "AsiDGo8e1QhVmxjQYVTUWIFtBfo1",
                likers = listOf("uid1"),
                dislikers = listOf("uid2"),
                commentators = listOf("uid1", "uid2")
            )
        )
    /*repository.take(DEFAULT_QUERY_LIMIT.toLong())
        .mapNotNull { obj -> obj.toSubject()?.withId(obj.id) }*/


    override suspend fun addAndGetId(item: Subject): String {
        val document = repository
            .collection
            .document()

        addWithId(item, document.id).await()
        return document.id
    }

    override fun addWithId(item: Subject, withId: String): Task<Void> =
        repository.add(item.withId(withId))

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