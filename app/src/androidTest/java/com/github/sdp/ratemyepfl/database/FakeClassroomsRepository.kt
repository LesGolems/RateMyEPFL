package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import java.time.LocalDate
import javax.inject.Inject

class FakeClassroomsRepository @Inject constructor(): ClassroomsRepositoryInterface {

    override suspend fun get(): List<Classroom?> {
        return listOf(
            Classroom(
                id = "CM3",
                name = "Salle"
            ),
            Classroom(
                id = "CE-1515",
                name = "Salle polyvalente"
            ),
            Classroom(
                id = "AAC 2 31",
                name = "Salle de cours"
            ),
            Classroom(
                id = "ELA 2",
                name = "Salle de cours"
            )
        )
    }

}