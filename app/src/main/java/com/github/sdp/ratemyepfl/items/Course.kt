package com.github.sdp.ratemyepfl.items

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val name: String,
    val faculty: String,
    val teacher: String,
    val credits: Int,
    val courseCode: String
) {

    override fun toString(): String = String.format("%s %s", courseCode, name)
}