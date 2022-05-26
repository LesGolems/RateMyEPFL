package com.github.sdp.ratemyepfl.backend.database.firebase.post

import com.github.sdp.ratemyepfl.model.review.Comment

interface CommentRepository : PostRepository<Comment> {
    /**
     * Retrieve the list of comments of a subject from its id
     *
     * @return the list non-null comments of the subject
     */
    suspend fun getBySubjectId(id: String?): List<Comment>
}