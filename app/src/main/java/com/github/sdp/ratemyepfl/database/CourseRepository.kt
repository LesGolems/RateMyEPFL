package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class CourseRepository @Inject constructor(db: FirebaseFirestore) : CourseRepositoryInterface,
    Repository(db, COURSE_COLLECTION_PATH) {

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
                .setId(id)
                .setNumReviews(getString(NUM_REVIEWS_FIELD)?.toInt())
                .setAverageGrade(getString(AVERAGE_GRADE_FIELD)?.toDouble())
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

    fun toItem(snapshot: DocumentSnapshot): Course? = snapshot.toCourse()

    override suspend fun getCourses(): List<Course> {
        return take(DEFAULT_LIMIT).mapNotNull { obj -> toItem(obj) }
    }

    override suspend fun getCourseById(id: String): Course? = toItem(getById(id))

    override fun updateCourseRating(id: String, rating: ReviewRating): Task<Unit> = updateRating(id, rating)

    fun add(course: Course) {
        collection.document(course.id).set(course.toHashMap())
    }

}