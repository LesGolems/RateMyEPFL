package com.github.sdp.ratemyepfl.model.items

import android.util.Log
import com.github.sdp.ratemyepfl.model.review.ClassroomReview
import com.google.firebase.firestore.DocumentSnapshot
import io.grpc.internal.JsonUtil.getList

class Classroom(val id: String, val name: String, val reviews: List<ClassroomReview> = listOf()){

    // This converts a json object from firebase into a Classroom object
    companion object {
        fun DocumentSnapshot.toClassroom() : Classroom? {
            return try {
                val id = getString("id")!!
                val name = getString("name")!!
                val reviews = get("reviews")!!
                // Not so nice cast
                Classroom(id, name, reviews as List<ClassroomReview>)
            } catch (e: Exception){
                Log.e(TAG, "Error converting classroom", e)
                null
            }
        }
        private const val TAG = "Classroom"
    }

}
