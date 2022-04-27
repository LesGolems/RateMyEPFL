package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.query.Query.Companion.DEFAULT_QUERY_LIMIT
import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.query.QueryResult.Companion.asQueryResult
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.AVERAGE_GRADE_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.NUM_REVIEWS_FIELD_NAME
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class CourseRepositoryImpl(val repository: ReviewableRepositoryImpl<Course>) : CourseRepository,
    ReviewableRepository<Course> by repository, Repository<Course> by repository {

    @Inject
    constructor(db: FirebaseFirestore) : this(
        ReviewableRepositoryImpl(
            db,
            COURSE_COLLECTION_PATH,
            COURSE_CODE_FIELD_NAME,
        ) { documentSnapshot ->
            documentSnapshot.toCourse()
        })

    companion object {
        const val COURSE_CODE_FIELD_NAME: String = "course_code"
        const val COURSE_COLLECTION_PATH = "courses"
        const val TITLE_FIELD_NAME = "title"
        const val SECTION_FIELD_NAME = "section"
        const val TEACHER_FIELD_NAME = "teacher"
        const val CREDITS_FIELD_NAME = "credits"
        const val CYCLE_FIELD_NAME = "cycle"
        const val SESSION_FIELD_NAME = "session"
        const val GRADING_FIELD_NAME = "grading"
        const val LANGUAGE_FIELD_NAME = "language"


        fun DocumentSnapshot.toCourse(): Course? {
            val builder = Course.Builder()
                .setCourseCode(getString(COURSE_CODE_FIELD_NAME))
                .setNumReviews(getField<Int>(NUM_REVIEWS_FIELD_NAME))
                .setAverageGrade(getDouble(AVERAGE_GRADE_FIELD_NAME))
                .setTitle(getString(TITLE_FIELD_NAME))
                .setSection(getString(SECTION_FIELD_NAME))
                .setTeacher(getString(TEACHER_FIELD_NAME))
                .setCredits(getField<Int>(CREDITS_FIELD_NAME))
                .setCycle(getString(CYCLE_FIELD_NAME))
                .setSession(getString(SESSION_FIELD_NAME))
                .setGrading(getString(GRADING_FIELD_NAME))
                .setLanguage(getString(LANGUAGE_FIELD_NAME))

            return try {
                builder.build()
            } catch (e: IllegalStateException) {
                null
            }
        }

    }

    @Deprecated(
        "This function has no signification anymore. Use load and its derived " +
                "functions instead"
    )
    override suspend fun getCourses(): List<Course> =
        repository
            .take(DEFAULT_QUERY_LIMIT.toLong()).mapNotNull { obj -> obj.toCourse() }

    override suspend fun getCourseById(id: String): Course? =
        repository
            .getById(id).toCourse()

    override suspend fun updateCourseRating(id: String, rating: ReviewRating) =
        repository
            .updateRating(id, rating)

    override fun search(prefix: String): QueryResult<List<Course>> {
        val byId = repository.search(COURSE_CODE_FIELD_NAME, prefix)
        val byTitle = repository.search(TITLE_FIELD_NAME, prefix)
        // Merge the two flows and map the content to Course
        return listOf(byId, byTitle).merge()
            .asQueryResult()
    }


}