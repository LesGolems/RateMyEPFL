package com.github.sdp.ratemyepfl.placeholder

import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Course.Companion.toCourse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CoursesRepository @Inject constructor(): Repository() {

    private val collection = db.collection("courses")
    companion object {
        private const val TAG = "CourseRepository"
    }

    suspend fun get() : List<Course?> {
        return collection.get().await().mapNotNull{obj -> obj.toCourse()}
    }

    suspend fun getById(id: String) : Course?{
        return collection.document(id).get().await().toCourse()
    }

}