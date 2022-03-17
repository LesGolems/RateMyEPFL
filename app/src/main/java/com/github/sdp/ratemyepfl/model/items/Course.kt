package com.github.sdp.ratemyepfl.model.items

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

    /**
     * For compatibility issues with the current code and the data format in the database
     */
    val name: String = title
    val faculty: String = section
    val courseCode: String = id

    constructor(name: String, faculty: String, teacher: String, credits: Int, courseCode: String)
            : this(name, faculty, teacher, credits, courseCode, 0, 0.0)

    override fun toString(): String {
        return "$id $title"
    }
}