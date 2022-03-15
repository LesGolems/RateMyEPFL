package com.github.sdp.ratemyepfl.placeholder

import android.util.Log
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Classroom.Companion.toClassroom
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// TODO: refactor using listeners
class ClassroomsRepository (private val db : FirebaseFirestore) {


    companion object {
        private const val TAG = "ClassroomRepository"
    }

    suspend fun getAll() : List<Classroom> {
        return db.collection("classrooms").get()
            .addOnSuccessListener {
                Log.i("Firebase", "Classrooms successfully fetched")
            }.addOnFailureListener{
                Log.i("Firebase", "Error getting all classrooms", it)
            } .await().toObjects(Classroom::class.java)
    }

    suspend fun getById(id: String) : Classroom?{
        return db.collection("classrooms").document(id).get()
            .addOnSuccessListener {
                Log.i("Firebase", "Got Classroom $id")
            }.addOnFailureListener {
                Log.i("Firebase", "Error getting Classroom", it)
            }.await().toClassroom()
        }


}