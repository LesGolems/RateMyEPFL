package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import javax.inject.Inject

class FakeCourseRepository @Inject constructor() : CourseRepositoryInterface {

    companion object {
        val COURSE_LIST = listOf(
            Course(
                title = "Software development project",
                section = "IC",
                teacher = "George Candea",
                credits = 4,
                id = "CS-306"
            ),
            Course(
                title = "Calcul quantique",
                section = "IC",
                teacher = "Nicolas Macris",
                credits = 4,
                id = "CS-308"
            ),
            Course(
                title = "Intelligence artificielle",
                section = "IC",
                teacher = "Boi Faltings",
                credits = 4,
                id = "CS-330"
            ),
            Course(
                title = "Projet de systems-on-chip",
                section = "IC",
                teacher = "René Beuchat",
                credits = 3,
                id = "CS-309"
            ),
            Course(
                title = "Introduction to database systems",
                section = "IC",
                teacher = "Christoph Koch",
                credits = 4,
                id = "CS-332"
            )
        )

        val COURSE_BY_ID = Course(
            title = "Software development project",
            section = "IC",
            teacher = "George Candea",
            credits = 4,
            id = "CS-306"
        )
    }


    override suspend fun getCourses(): List<Course> = COURSE_LIST

    override suspend fun getCourseById(id: String): Course = COURSE_BY_ID
    override fun updateCourseRating(id: String, rating: ReviewRating) {
    }

}