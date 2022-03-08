package com.github.sdp.ratemyepfl.placeholder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.sdp.ratemyepfl.Classroom
import com.github.sdp.ratemyepfl.review.ClassroomReview

class DataSource() {
    private val initialRoomsList = roomsList()
    private val roomsLiveData = MutableLiveData(initialRoomsList)

    private fun roomsList(): List<Classroom> {
        return listOf(
            Classroom(
                id = "CM3",
                name = "Salle",
                reviews = mutableListOf(
                    ClassroomReview(15, "bien"),
                    ClassroomReview(16, "pas mal du tout")
                )
            ),
            Classroom(
                id = "CE-1515",
                name = "Salle polyvalente",
                reviews = mutableListOf(
                    ClassroomReview(17, "en vrai c ienb"),
                    ClassroomReview(20, "excellent")
                )
            )
        )
    }

    fun getRoomForId(id: String?): Classroom? {
        roomsLiveData.value?.let { rooms ->
            return rooms.firstOrNull { it.id == id }
        }
        return null
    }


    fun getRoomList(): LiveData<List<Classroom>> {
        return roomsLiveData
    }

}

