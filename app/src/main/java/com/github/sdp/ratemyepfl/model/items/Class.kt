package com.github.sdp.ratemyepfl.model.items

class Class (
    val id: Int? = null,
    val name: String? = null,
    val teacher: String? = null,
    val room: Classroom? = null,
    val day: Int? = null,
    val startTime: Int? = null,
    val endTime: Int? = null) {

    val duration = (endTime ?: 0) - (startTime ?: 0)

}
