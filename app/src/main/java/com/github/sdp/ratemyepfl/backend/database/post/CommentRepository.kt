package com.github.sdp.ratemyepfl.backend.database.post

import com.github.sdp.ratemyepfl.model.review.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository : PostRepository<Comment> {
    /**
     * Retrieve the list of comments of a subject from its id
     *
     * @return the list non-null comments of the subject
     */
    fun getBySubjectId(id: String): Flow<List<Comment>>
}