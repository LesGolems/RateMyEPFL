package com.github.sdp.ratemyepfl.database.post

import com.github.sdp.ratemyepfl.model.review.Subject

interface SubjectRepository : PostRepository<Subject> {

    /**
     * Retrieve the reviews from the repository
     *
     * @return a list of non-null reviews
     */
    suspend fun getSubjects(): List<Subject>

    /**
     * Adds a comment to the subject with id [subjectId].
     * There are no restriction on the number of comments for a user

     * @param subjectId: id of the subject
     * @param commentId: TODO
     */
    suspend fun addComment(subjectId: String, commentId: String)

    /**
     * Adds a comment to the subject with id [subjectId].
     * There are no restriction on the number of comments for a user
     *
     * @param subjectId: id of the subject
     * @param commentId: TODO
     */
    suspend fun removeComment(subjectId: String, commentId: String)

}