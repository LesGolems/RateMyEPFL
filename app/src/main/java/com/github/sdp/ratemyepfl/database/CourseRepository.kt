package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Course
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class CourseRepository @Inject constructor() : ItemRepositoryImpl<Course>(COURSE_COLLECTION_PATH) {

    companion object {
        const val COURSE_COLLECTION_PATH = "courses"
    }

    override fun toItem(snapshot: DocumentSnapshot): Course? {
        val builder = Course.Builder()
            .setId(snapshot.id)
            .setTitle(snapshot.getString("title"))
            .setSection(snapshot.getString("section"))
            .setTeacher(snapshot.getString("teacher"))
            .setCredits(snapshot.getString("credits")?.toInt())

        return try {
            builder.build()
        } catch (e: IllegalStateException) {
            null
        }
    }
}