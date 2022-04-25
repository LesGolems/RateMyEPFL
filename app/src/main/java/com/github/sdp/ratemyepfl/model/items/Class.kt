package com.github.sdp.ratemyepfl.model.items

data class Class (
    val id: Int? = null,
    val name: String? = null,
    val teacher: String? = null,
    val room: Classroom? = null,
    val day: Int? = null,
    val start: Int? = null,
    val end: Int? = null) {

    val duration = (end ?: 0) - (start ?: 0)

}
