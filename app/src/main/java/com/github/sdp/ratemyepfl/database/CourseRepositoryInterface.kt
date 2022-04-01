package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Course

interface CourseRepositoryInterface {
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