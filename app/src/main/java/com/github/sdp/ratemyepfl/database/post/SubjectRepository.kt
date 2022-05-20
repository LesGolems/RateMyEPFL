package com.github.sdp.ratemyepfl.database.post

import com.github.sdp.ratemyepfl.model.review.Subject

interface SubjectRepository : PostRepository<Subject> {

    /**
     * Retrieve the reviews from the repository
     *
     * @return a list of non-null reviews
     */
    suspend fun getSubjects(): List<Subject>

}