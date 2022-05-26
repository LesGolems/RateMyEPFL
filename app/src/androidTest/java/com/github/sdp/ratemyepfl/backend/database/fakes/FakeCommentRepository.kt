package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.backend.database.post.CommentRepository
import com.github.sdp.ratemyepfl.model.review.Comment
import com.github.sdp.ratemyepfl.model.time.DateTime
import com.google.android.gms.tasks.Task
import org.mockito.Mockito
import javax.inject.Inject

class FakeCommentRepository @Inject constructor() : CommentRepository, FakeRepository<Comment>() {

    companion object {
        private val COMMENT_LIST = listOf(
            Comment("id1", "comment1", DateTime.now(), "12345"),
            Comment("id2", "comment2", DateTime.now()),
            Comment("id3", "comment3", DateTime.now()),
        )
        var commentList = COMMENT_LIST
    }

    override suspend fun getBySubjectId(id: String?): List<Comment> = commentList

    @Suppress("UNCHECKED_CAST")
    override fun remove(id: String): Task<Void> {
        val newList = arrayListOf<Comment>()

        for (c in commentList) {
            if (c.getId() != id) {
                newList.add(c)
            }
        }

        commentList = newList

        return Mockito.mock(Task::class.java) as Task<Void>
    }

    override fun addWithId(item: Comment, withId: String): Task<String> {
        TODO("Not yet implemented")
    }

    override suspend fun addUpVote(postId: String, userId: String) {
    }

    override suspend fun removeUpVote(postId: String, userId: String) {
    }

    override suspend fun addDownVote(postId: String, userId: String) {
    }

    override suspend fun removeDownVote(postId: String, userId: String) {
    }

    override suspend fun take(number: Long): List<Comment> {
        TODO("Not yet implemented")
    }
}