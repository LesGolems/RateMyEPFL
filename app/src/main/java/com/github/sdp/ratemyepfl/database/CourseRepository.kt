package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Course.Companion.toCourse
import com.google.firebase.firestore.DocumentSnapshot

class CourseRepository: ItemsRepository<Course>(COURSE_COLLECTION_PATH) {

    companion object {
        const val COURSE_COLLECTION_PATH = "courses"
    }

    override fun toItem(snapshot: DocumentSnapshot): Course? = snapshot.toCourse()
}