package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.model.review.ClassroomReview
import com.google.firebase.firestore.DocumentSnapshot

class Classroom(val id: String, val name: String, val reviews: List<ClassroomReview> = listOf()){

    // This converts a json object from firebase into a Classroom object
    companion object {
        fun DocumentSnapshot.toClassroom() : Classroom? {
            return try {
                val id = getString("id")!!
                val name = getString("name")!!
                Classroom(id, name,  listOf())
            } catch (e: Exception){
                null
            }
        }
        private const val TAG = "Classroom"
    }

}
