package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.R
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

    }

    override val layoutReview = R.layout.activity_course_review
}