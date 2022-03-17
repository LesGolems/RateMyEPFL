package com.github.sdp.ratemyepfl.model.items

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val name: String,
    val faculty: String,
    val teacher: String,
    val credits: Int,
    val courseCode: String
) {
    override fun toString(): String {
        return "$courseCode $name"
    }

    companion object {
        fun DocumentSnapshot.toCourse() : Course? {
            return try {
                val name = getString("title")!!
                //val faculty = getString("faculty")!!
                val teacher = getString("teacher")!!
                //val credits = getString("credits")?.toInt()!!
                Course(name, "", teacher, 4, id)
            } catch (e: Exception){
                null
            }
        }
        private const val TAG = "Course"
    }

}