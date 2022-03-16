package com.github.sdp.ratemyepfl.placeholder

import android.util.Log
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course
import com.github.sdp.ratemyepfl.model.items.Course.Companion.toCourse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class CoursesRepository : Repository() {

    private val collection = db.collection("courses")
    companion object {
        private const val TAG = "CourseRepository"
    }

    fun getAll() : Flow<Course?>  = flow {
        collection.get().addOnSuccessListener {
                Log.i("Firebase", "Courses successfully fetched")
            }.addOnFailureListener{
                Log.i("Firebase", "Error getting all courses", it)
            }.await().asFlow().collect { value -> emit(value.toCourse()) }
    }

    suspend fun getById(id: String) : Course?{
        return collection.document(id).get()
            .addOnSuccessListener {
                Log.i("Firebase", "Got Course $id")
            }.addOnFailureListener {
                Log.i("Firebase", "Error getting courses", it)
            }.await().toCourse()
    }

}