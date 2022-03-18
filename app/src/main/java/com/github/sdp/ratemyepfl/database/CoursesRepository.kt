package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Course.Companion.toCourse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CoursesRepository @Inject constructor(): CoursesRepositoryInterface, Repository() {
    private val collection = db.collection("courses")
    companion object {
        private const val TAG = "CourseRepository"
    }

    override suspend fun get() : List<Course?> {
        return collection.limit(50).get().await().mapNotNull{obj -> obj.toCourse()}
    }

    suspend fun getById(id: String) : Course?{
        return collection.document(id).get().await().toCourse()
    }

}