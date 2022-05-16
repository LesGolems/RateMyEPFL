package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.LoaderRepository
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepository
import com.github.sdp.ratemyepfl.database.reviewable.CourseRepositoryImpl
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Course
import javax.inject.Inject

class FakeCourseRepository @Inject constructor(val repository: FakeLoaderRepository<Course>) :
    CourseRepository,
    ReviewableRepository<Course>, LoaderRepository<Course> by repository {

    override val offlineData: List<Course> = CourseRepositoryImpl.OFFLINE_COURSES

    companion object {
        val COURSE_LIST = listOf(
            Course(
                title = "Software development project",
                section = "IC",
                teacher = "George Candea",
                credits = 4,
                courseCode = "CS-306",
                grade = 5.0,
                numReviews = 1
            ),
            Course(
                title = "Calcul quantique",
                section = "IC",
                teacher = "Nicolas Macris",
                credits = 4,
                courseCode = "CS-308",
                grade = 5.0,
                numReviews = 1
            ),
            Course(
                title = "Intelligence artificielle",
                section = "IC",
                teacher = "Boi Faltings",
                credits = 4,
                courseCode = "CS-330",
                grade = 5.0,
                numReviews = 1
            ),
            Course(
                title = "Projet de systems-on-chip",
                section = "IC",
                teacher = "René Beuchat",
                credits = 3,
                courseCode = "CS-309",
                grade = 5.0,
                numReviews = 1
            ),
            Course(
                title = "Introduction to database systems",
                section = "IC",
                teacher = "Christoph Koch",
                credits = 4,
                courseCode = "CS-332",
                grade = 5.0,
                numReviews = 1
            )
        )

        val COURSE_NO_REVIEW = Course(
            title = "Software development project",
            section = "IC",
            teacher = "George Candea",
            credits = 4,
            courseCode = "CS-306",
            0.0,
            0
        )

        val COURSE_WITH_REVIEW = Course(
            title = "Software development project",
            section = "IC",
            teacher = "George Candea",
            credits = 4,
            courseCode = "CS-306",
            5.5,
            2
        )

        var courseById = COURSE_NO_REVIEW
    }


    override suspend fun getCourses(): List<Course> = COURSE_LIST

    override suspend fun getCourseById(id: String): Course = courseById

}