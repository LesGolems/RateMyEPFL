package com.github.sdp.ratemyepfl.backend.database.reviewable

import com.github.sdp.ratemyepfl.model.items.Course

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
     * @return the course if found
     *
     * @throws NoSuchElementException if no elemnt with the provided id exists
     */
    suspend fun getCourseByCourseCode(courseCode: String): Course

}