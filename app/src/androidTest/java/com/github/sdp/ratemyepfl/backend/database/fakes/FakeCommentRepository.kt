package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.backend.database.firebase.post.CommentRepository
import com.github.sdp.ratemyepfl.backend.database.query.FirebaseQuery
import com.github.sdp.ratemyepfl.model.review.Comment
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class FakeCommentRepository @Inject constructor() : CommentRepository, FakeRepository<Comment>() {
    override suspend fun getBySubjectId(id: String?): List<Comment> {
        TODO("Not yet implemented")
    }

    override suspend fun addAndGetId(item: Comment): String {
        TODO("Not yet implemented")
    }

    override fun addWithId(item: Comment, withId: String): Task<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addUpVote(postId: String, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun removeUpVote(postId: String, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun addDownVote(postId: String, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun removeDownVote(postId: String, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun take(number: Long): List<Comment> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: String): Comment? {
        TODO("Not yet implemented")
    }

    override fun remove(id: String): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun add(item: Comment): Task<String> {
        TODO("Not yet implemented")
    }

    override fun update(id: String, transform: (Comment) -> Comment): Task<Comment> {
        TODO("Not yet implemented")
    }

    override fun transform(document: DocumentSnapshot): Comment? {
        TODO("Not yet implemented")
    }

    override fun query(): FirebaseQuery {
        TODO("Not yet implemented")
    }
}