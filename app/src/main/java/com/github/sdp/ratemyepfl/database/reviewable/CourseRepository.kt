package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.LoaderRepository
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.ReviewRating

interface CourseRepository : ReviewableRepository<Course> {
    /**
     * Retrieve the course from the repository
     *
     * @return a list of non-null course
     */
    suspend fun getCourses(): List<Course>

    /**
     * Retrieve an course from id.
     *
     * @return the course if found, otherwise null
     */
    suspend fun getCourseById(id: String): Course?

    /**
     * Updates the rating of the course using a transaction for concurrency
     *
     *  @param id : id of the reviewed item
     *  @param rating: rating of the review being added
     */
    suspend fun updateCourseRating(id: String, rating: ReviewRating)

}