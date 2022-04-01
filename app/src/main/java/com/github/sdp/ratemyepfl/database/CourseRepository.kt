package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class CourseRepository @Inject constructor() : CourseRepositoryInterface, Repository(COURSE_COLLECTION_PATH) {

    companion object {
        const val COURSE_COLLECTION_PATH = "courses"
        const val TITLE_FIELD_NAME = "title"
        const val SECTION_FIELD_NAME = "section"
        const val TEACHER_FIELD_NAME = "teacher"
        const val CREDITS_FIELD_NAME = "credits"

        fun DocumentSnapshot.toCourse(): Course? {
            val builder = Course.Builder()
                .setId(id)
                .setTitle(getString(TITLE_FIELD_NAME))
                .setSection(getString(SECTION_FIELD_NAME))
                .setTeacher(getString(TEACHER_FIELD_NAME))
                .setCredits(getString(CREDITS_FIELD_NAME)?.toInt())

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

}