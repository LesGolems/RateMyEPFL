package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.SearchableRepository
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.ReviewRating

interface CourseRepository : SearchableRepository<Course> {
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

}