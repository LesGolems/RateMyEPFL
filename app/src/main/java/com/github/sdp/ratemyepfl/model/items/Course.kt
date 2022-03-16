package com.github.sdp.ratemyepfl.model.items

import android.util.Log
import com.github.sdp.ratemyepfl.model.review.ClassroomReview
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable
import java.lang.reflect.Array.getInt

@Serializable
class Course(
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
                val name = getString("name")!!
                val faculty = getString("faculty")!!
                val teacher = getString("teacher")!!
                val credits = getString("credits")?.toInt()!!
                val courseCode = getString("courseCode")!!
                Course(name, faculty, teacher, credits, courseCode)
            } catch (e: Exception){
                Log.e(TAG, "Error converting course", e)
                null
            }
        }
        private const val TAG = "Course"
    }

}