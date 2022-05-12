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
                grade = 5.0
            ),
            Course(
                title = "Calcul quantique",
                section = "IC",
                teacher = "Nicolas Macris",
                credits = 4,
                courseCode = "CS-308",
                grade = 5.0
            ),
            Course(
                title = "Intelligence artificielle",
                section = "IC",
                teacher = "Boi Faltings",
                credits = 4,
                courseCode = "CS-330",
                grade = 5.0
            ),
            Course(
                title = "Projet de systems-on-chip",
                section = "IC",
                teacher = "René Beuchat",
                credits = 3,
                courseCode = "CS-309",
                grade = 5.0
            ),
            Course(
                title = "Introduction to database systems",
                section = "IC",
                teacher = "Christoph Koch",
                credits = 4,
                courseCode = "CS-332",
                grade = 5.0
            )
        )

        var courseById = Course(
            title = "Software development project",
            section = "IC",
            teacher = "George Candea",
            credits = 4,
            courseCode = "CS-306",
            0.0,
        )
    }


    override suspend fun getCourses(): List<Course> = COURSE_LIST

    override suspend fun getCourseById(id: String): Course = courseById

    override fun search(prefix: String): QueryResult<List<Course>> = QueryResult(
        flow { emit(QueryState.success(listOf())) }
    )
}