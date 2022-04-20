package com.github.sdp.ratemyepfl.database.reviewable

import com.github.sdp.ratemyepfl.database.QueryResult
import com.github.sdp.ratemyepfl.database.QueryResult.Companion.asQueryResult
import com.github.sdp.ratemyepfl.database.Repository
import com.github.sdp.ratemyepfl.database.Repository.Companion.DEFAULT_QUERY_LIMIT
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.AVERAGE_GRADE_FIELD_NAME
import com.github.sdp.ratemyepfl.database.reviewable.ReviewableRepositoryImpl.Companion.NUM_REVIEWS_FIELD_NAME
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class CourseRepositoryImpl(val repository: ReviewableRepositoryImpl<Course>) : CourseRepository,
    ReviewableRepository<Course> by repository, Repository<Course> by repository {

    @Inject
    constructor(db: FirebaseFirestore) : this(
        ReviewableRepositoryImpl(
            db,
            COURSE_COLLECTION_PATH
        ) { documentSnapshot ->
            documentSnapshot.toCourse()
        })

    companion object {
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
                .setCourseCode(id)
                .setNumReviews(getString(NUM_REVIEWS_FIELD_NAME)?.toInt())
                .setAverageGrade(getString(AVERAGE_GRADE_FIELD_NAME)?.toDouble())
                .setTitle(getString(TITLE_FIELD_NAME))
                .setSection(getString(SECTION_FIELD_NAME))
                .setTeacher(getString(TEACHER_FIELD_NAME))
                .setCredits(getString(CREDITS_FIELD_NAME)?.toInt())
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
            .take(DEFAULT_QUERY_LIMIT).mapNotNull { obj -> obj.toCourse() }

    override suspend fun getCourseById(id: String): Course? =
        repository
            .getById(id).toCourse()

    override suspend fun updateCourseRating(id: String, rating: ReviewRating) =
        repository
            .updateRating(id, rating)

    override fun search(pattern: String): QueryResult<List<Course>> {
        val byId = repository.search(pattern)
        val byTitle = repository.search(pattern, TITLE_FIELD_NAME)
        // Merge the two flows and map the content to Course
        return listOf(byId, byTitle).merge()
            .asQueryResult()
    }


}