package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepository
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeCourseRepository @Inject constructor() : CourseRepository {

    companion object {
        val COURSE_LIST = listOf(
            Course(
                title = "Software development project",
                section = "IC",
                teacher = "George Candea",
                credits = 4,
                courseCode = "CS-306",
                numReviews = 15,
                averageGrade = 2.5
            ),
            Course(
                title = "Calcul quantique",
                section = "IC",
                teacher = "Nicolas Macris",
                credits = 4,
                courseCode = "CS-308",
                numReviews = 15,
                averageGrade = 2.5
            ),
            Course(
                title = "Intelligence artificielle",
                section = "IC",
                teacher = "Boi Faltings",
                credits = 4,
                courseCode = "CS-330",
                numReviews = 15,
                averageGrade = 2.5
            ),
            Course(
                title = "Projet de systems-on-chip",
                section = "IC",
                teacher = "René Beuchat",
                credits = 3,
                courseCode = "CS-309",
                numReviews = 15,
                averageGrade = 2.5
            ),
            Course(
                title = "Introduction to database systems",
                section = "IC",
                teacher = "Christoph Koch",
                credits = 4,
                courseCode = "CS-332",
                numReviews = 15,
                averageGrade = 2.5
            )
        )

        val COURSE_WITH_REVIEWS = Course(
            title = "Software development project",
            section = "IC",
            teacher = "George Candea",
            credits = 4,
            courseCode = "CS-306",
            numReviews = 15,
            averageGrade = 2.5
        )

        val COURSE_WITHOUT_REVIEWS = Course(
            title = "Software development project",
            section = "IC",
            teacher = "George Candea",
            credits = 4,
            courseCode = "CS-306",
            numReviews = 0,
            averageGrade = 0.0
        )

        var courseById = COURSE_WITH_REVIEWS
    }


    override suspend fun getCourses(): List<Course> = COURSE_LIST

    override suspend fun getCourseById(id: String): Course = courseById

    override suspend fun updateCourseRating(id: String, rating: ReviewRating) {}

    override fun search(prefix: String): QueryResult<List<Course>> = QueryResult(
        flow { emit(QueryState.success(listOf())) }
    )
}