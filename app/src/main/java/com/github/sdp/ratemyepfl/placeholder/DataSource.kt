package com.github.sdp.ratemyepfl.placeholder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.ClassroomReview
import java.time.LocalDate
import javax.inject.Inject

class DataSource @Inject constructor() {
    private val initialRoomsList = roomsList()
    private val roomsLiveData = MutableLiveData(initialRoomsList)

    private fun roomsList(): List<Classroom> {
        return listOf(
            Classroom(
                id = "CM3",
                name = "Salle",
                reviews = mutableListOf(
                    ClassroomReview(15, "bien", LocalDate.now()),
                    ClassroomReview(16, "pas mal du tout", LocalDate.now())
                ),
            ),
            Classroom(
                id = "CE-1515",
                name = "Salle polyvalente",
                reviews = mutableListOf(
                    ClassroomReview(17, "en vrai c ienb", LocalDate.now()),
                    ClassroomReview(20, "excellent", LocalDate.now())
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

