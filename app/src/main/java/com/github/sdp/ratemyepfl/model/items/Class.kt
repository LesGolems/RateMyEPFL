package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.Serializable

@Serializable
data class Class(
    val id: Int? = null,
    val name: String? = null,
    val teacher: String? = null,
    val room: String? = null,
    val day: Int? = null,
    val start: Int? = null,
    val end: Int? = null
) {

    fun duration() = (end ?: 0) - (start ?: 0)

}
