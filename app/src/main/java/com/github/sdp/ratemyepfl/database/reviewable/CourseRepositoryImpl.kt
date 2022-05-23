package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.LoaderRepository
import com.github.sdp.ratemyepfl.database.LoaderRepositoryImpl
import com.github.sdp.ratemyepfl.database.RepositoryImpl
import com.github.sdp.ratemyepfl.database.RepositoryImpl.Companion.toItem
import com.github.sdp.ratemyepfl.database.query.OrderDirection
import com.github.sdp.ratemyepfl.database.query.Query
import com.github.sdp.ratemyepfl.model.items.Course
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class CourseRepositoryImpl private constructor(private val repository: LoaderRepository<Course>) :
    CourseRepository,
    ReviewableRepository<Course>,
    LoaderRepository<Course> by repository {

    override val offlineData: List<Course> = OFFLINE_COURSES

    @Inject
    constructor(db: FirebaseFirestore) : this(
        LoaderRepositoryImpl<Course>(
            RepositoryImpl(db, COURSE_COLLECTION_PATH)
            { documentSnapshot ->
                documentSnapshot.toCourse()
            })
    )

    companion object {
        const val COURSE_CODE_FIELD_NAME: String = "courseCode"
        const val COURSE_COLLECTION_PATH = "courses"
        const val TITLE_FIELD_NAME = "title"
        const val SECTION_FIELD_NAME = "section"
        const val TEACHER_FIELD_NAME = "teacher"
        const val CREDITS_FIELD_NAME = "credits"
        const val CYCLE_FIELD_NAME = "cycle"
        const val SESSION_FIELD_NAME = "session"
        const val GRADING_FIELD_NAME = "grading"
        const val LANGUAGE_FIELD_NAME = "language"

        val OFFLINE_COURSES = listOf<Course>(
            Course(
                title = "Advanced information, computation, communication I",
                section = "IC",
                teacher = "Karl Aberer",
                credits = 7,
                courseCode = "CS-101",
                grade = 2.5,
                1
            ),
            Course(
                title = "Introduction à la programmation",
                section = "IC",
                teacher = "Jamila Sam",
                credits = 5,
                courseCode = "CS-107",
                grade = 2.5,
                1
            ),
            Course(
                title = "Pratique de la programmation orientée objet",
                section = "IC",
                teacher = "Michel Schinz",
                credits = 9,
                courseCode = "CS-108",
                grade = 2.5,
                1
            ),
            Course(
                title = "Digital system design",
                section = "IC",
                teacher = "Kluter Ties Jan Henderikus",
                credits = 6,
                courseCode = "CS-173",
                grade = 2.5,
                1
            ),
            Course(
                title = "Software development project",
                section = "IC",
                teacher = "Candea George",
                credits = 4,
                courseCode = "CS-306",
                grade = 2.5,
                1
            ),
            Course(
                title = "Analyse I",
                section = "IC",
                teacher = "Lachowska Anna",
                credits = 6,
                courseCode = "MATH-101(e)",
                grade = 2.5,
                1
            ),
            Course(
                title = "Analyse II",
                section = "IC",
                teacher = "Lachowska Anna",
                credits = 6,
                courseCode = "MATH-106(e)",
                grade = 2.5,
                1
            )
        )

        fun DocumentSnapshot.toCourse(): Course? = toItem()

    }

    private val loadQuery = repository
        .query()
        .orderBy(ReviewableRepository.GRADE_FIELD_NAME, OrderDirection.DESCENDING)
        .orderBy(COURSE_CODE_FIELD_NAME)


    override suspend fun getCourses(): List<Course> =
        repository
            .take(Query.DEFAULT_QUERY_LIMIT.toLong())

    override suspend fun getCourseById(id: String): Course? =
        repository
            .getById(id)


}