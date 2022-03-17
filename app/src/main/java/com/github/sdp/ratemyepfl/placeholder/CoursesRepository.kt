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

    suspend fun get() : List<Course?> {
        return collection.get().await().mapNotNull{obj -> obj.toCourse()}
    }

    /*fun getPosts(userId: String): Flow<List<Post>> {
        val db = FirebaseFirestore.getInstance()
        return callbackFlow {
            val listenerRegistration = db.collection("users")
                .document(userId)
                .collection("posts")
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(message = "Error fetching posts",
                            cause = firebaseFirestoreException)
                        return@addSnapshotListener
                    }
                    val map = querySnapshot.documents.
                        .mapNotNull { it.toPost() }
                    offer(map)
                }
            awaitClose {
                Log.d(TAG, "Cancelling posts listener")
                listenerRegistration.remove()
            }
        }*/


    /*suspend fun getById(id: String) : Course?{
        return collection.document(id).get()
            .addOnSuccessListener {
                Log.i("Firebase", "Got Course $id")
            }.addOnFailureListener {
                Log.i("Firebase", "Error getting courses", it)
            }.await().toCourse()
    }*/

}