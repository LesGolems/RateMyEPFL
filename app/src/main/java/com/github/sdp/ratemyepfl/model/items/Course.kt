package com.github.sdp.ratemyepfl.model.items

import com.github.sdp.ratemyepfl.database.reviewable.CourseRepositoryImpl
import com.google.firebase.firestore.PropertyName
import kotlinx.serialization.Serializable

@Serializable
data class Course constructor(
    val title: String = "",
    val section: String = "",
    val teacher: String = "",
    val credits: Int = 0,
    val courseCode: String = "",
    override val grade: Double = 0.0,
    override val numReviews: Int = 0,
    val cycle: String = "",
    val session: String = "",
    val grading: String = "",
    val language: String = "",
) : Reviewable() {

    override fun toString(): String {
        return "$courseCode $title"
    }

    override fun getId(): String = courseCode
}