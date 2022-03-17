package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Classroom.Companion.toClassroom
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClassroomsRepository @Inject constructor(): ClassroomsRepositoryInterface {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("classrooms")
    companion object {
        private const val TAG = "ClassroomRepository"
    }

    override suspend fun get() : List<Classroom?> {
        return collection.get().await().mapNotNull{obj -> obj.toClassroom()}
    }

    suspend fun getById(id: String) : Classroom?{
        return collection.document(id).get().await().toClassroom()
    }

}