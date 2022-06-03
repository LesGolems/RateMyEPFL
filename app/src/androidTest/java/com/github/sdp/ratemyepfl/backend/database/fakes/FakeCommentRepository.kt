package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.backend.database.post.CommentRepository
import com.github.sdp.ratemyepfl.model.review.Comment
import com.github.sdp.ratemyepfl.model.time.DateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeCommentRepository @Inject constructor() : CommentRepository, FakeRepository<Comment>() {

    companion object {
        private val COMMENT_LIST = listOf(
            Comment("", "id1", "comment1", DateTime.now(), "12345"),
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

    override fun addWithId(item: Comment, withId: String): Flow<String> {
        elements = elements.plus(item)
        return flowOf(withId)
    }

    override suspend fun addUpVote(postId: String, userId: String) {
        elements.map {
            if (it.getId() == postId) {
                it.copy(likers = it.likers.plus(userId))
            } else it
        }
    }

    override suspend fun removeUpVote(postId: String, userId: String) {
        elements.map {
            if (it.getId() == postId) {
                it.copy(likers = it.likers.minus(userId))
            } else it
        }
    }

    override suspend fun addDownVote(postId: String, userId: String) {
        elements.map {
            if (it.getId() == postId) {
                it.copy(likers = it.dislikers.plus(userId))
            } else it
        }
    }

    override suspend fun removeDownVote(postId: String, userId: String) {
        elements.map {
            if (it.getId() == postId) {
                it.copy(likers = it.dislikers.minus(userId))
            } else it
        }
    }
}