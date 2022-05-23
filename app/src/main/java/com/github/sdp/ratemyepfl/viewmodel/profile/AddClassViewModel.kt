package com.github.sdp.ratemyepfl.viewmodel.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.ratemyepfl.exceptions.MissingInputException
import com.github.sdp.ratemyepfl.model.items.Class
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.items.Course

/**
 * View model used to share data between select fragments and add class fragment for class creation
 */
class AddClassViewModel : ViewModel() {

    val course: MutableLiveData<Course?> = MutableLiveData(null)
    val room: MutableLiveData<Classroom?> = MutableLiveData(null)
    val day: MutableLiveData<Int?> = MutableLiveData(null)
    val startHour: MutableLiveData<Int> = MutableLiveData(0)
    val endHour: MutableLiveData<Int> = MutableLiveData(0)

    fun createClass(): Class {
        val course = course.value
        val room = room.value
        val day = day.value
        val startHour = startHour.value
        val endHour = endHour.value

        if (course == null) {
            throw MissingInputException("You need to enter a course")
        }
        if (room == null) {
            throw MissingInputException("You need to enter a room")
        }
        if (day == null) {
            throw MissingInputException("You need to select a day")
        }
        return Class(
            course.getId(),
            course.title,
            course.teacher,
            room.name,
            day,
            startHour,
            endHour
        )
    }
}