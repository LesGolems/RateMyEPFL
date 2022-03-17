package com.github.sdp.ratemyepfl.model.items

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
}