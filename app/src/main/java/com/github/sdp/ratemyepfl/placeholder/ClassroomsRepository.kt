package com.github.sdp.ratemyepfl.placeholder

import android.util.Log
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Classroom.Companion.toClassroom
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

// TODO: refactor using listeners
class ClassroomsRepository : Repository() {

    private val collection = db.collection("classrooms")
    companion object {
        private const val TAG = "ClassroomRepository"
    }

    fun getAll() : Flow<Classroom?> = flow {
        collection.get().addOnSuccessListener{
                Log.i("Firebase", "Classrooms successfully fetched")
            }.addOnFailureListener{
                Log.i("Firebase", "Error getting all classrooms", it)
            }.await().asFlow().collect { value -> emit(value.toClassroom()) }
    }

    suspend fun getById(id: String) : Classroom?{
        return collection.document(id).get()
            .addOnSuccessListener {
                Log.i("Firebase", "Got Classroom $id")
            }.addOnFailureListener {
                Log.i("Firebase", "Error getting Classroom", it)
            }.await().toClassroom()
    }


}