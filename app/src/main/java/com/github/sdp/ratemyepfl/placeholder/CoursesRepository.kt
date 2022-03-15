package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.items.Course
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class CoursesRepository: Repository<Course>() {
    companion object {
        const val COURSES_COLLECTION = "courses"
    }


    override suspend fun add(value: Course) {
    }

    override suspend fun remove(value: Course) {
    }

    override suspend fun get(): List<Course> {
        return collectionReference().get().await().map { q -> toCourse(q.id, q["title"].toString()) }
    }

    private fun collectionReference(): CollectionReference {
        return db.collection(COURSES_COLLECTION)
    }

    private fun toCourse(code: String, name: String): Course {
        return Course(name, "", "", 4, code)
    }
}