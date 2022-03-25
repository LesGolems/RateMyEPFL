package com.github.sdp.ratemyepfl.model.items

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val title: String,
    val section: String,
    val teacher: String,
    val credits: Int,
    override val id: String,
    override var numRatings: Int = 0,
    override var avgRating: Double = 0.0,
    val cycle: String? = null,
    val session: String? = null,
    val grading: String? = null,
    val language: String? = null
) : Reviewable() {

    override fun toString(): String {
        return "$id $title"
    }

    companion object {
        fun DocumentSnapshot.toCourse(): Course? {
            return try {
                val title = getString("title")!!
                val section = getString("section")!!
                val teacher = getString("teacher")!!
                val credits = getString("credits")?.toInt()!!
                Course(title, section, teacher, credits, id)
            } catch (e: Exception) {
                null
            }
        }

        private const val TAG = "Course"
    }

    override fun toHashMap(): HashMap<String, String> {
        return hashMapOf(
            "id" to id, "numRatings" to numRatings.toString(), "avgRating" to avgRating.toString(),
            "title" to title, "section" to section, "teacher" to teacher, "credits" to credits.toString()
        )
    }

}