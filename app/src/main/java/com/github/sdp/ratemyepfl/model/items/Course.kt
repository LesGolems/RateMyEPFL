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
    val cycle: String? = null,
    val session: String? = null,
    val grading: String? = null,
    val language: String? = null
) : Reviewable() {

    override fun toString(): String {
        return "$id $title"
    }

    companion object {
        /**
         * Converts the json data in a Course object
         *
         * @return a course object if it contains the correct data, null otherwise
         */
        fun DocumentSnapshot.toCourse(): Course? {
            val title: String? = getString("title")
            val section: String? = getString("section")
            val teacher: String? = getString("teacher")
            val credits: Int? = getString("credits")?.toInt()

            return if (title != null && section != null && teacher != null && credits != null) {
                Course(title, section, teacher, credits, id)
            } else {
                null
            }
        }

    }

    override val collectionPath = "courses"
}