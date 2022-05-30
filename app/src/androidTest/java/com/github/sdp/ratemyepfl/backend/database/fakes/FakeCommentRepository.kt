package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.backend.database.post.CommentRepository
import com.github.sdp.ratemyepfl.model.review.Comment
import com.github.sdp.ratemyepfl.model.time.DateTime
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.Mockito
import javax.inject.Inject

class FakeCommentRepository @Inject constructor() : CommentRepository, FakeRepository<Comment>() {

    companion object {
        private val COMMENT_LIST = listOf(
            Comment("","id1", "comment1", DateTime.now(), "12345"),
            Comment("", "id2", "comment2", DateTime.now()),
            Comment("", "id3", "comment3", DateTime.now()),
        )
    }

    init {
        elements = COMMENT_LIST.toSet()
    }


        override fun getBySubjectId(id: String): Flow<List<Comment>> = flow {
        emit(elements.toList())
    }

    override fun addWithId(item: Comment, withId: String): Flow<String> = flowOf(withId)

    override suspend fun addUpVote(postId: String, userId: String) {
    }

    override suspend fun removeUpVote(postId: String, userId: String) {
    }

    override suspend fun addDownVote(postId: String, userId: String) {
    }

    override suspend fun removeDownVote(postId: String, userId: String) {
    }
}