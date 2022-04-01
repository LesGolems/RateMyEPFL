package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.model.items.Classroom
import javax.inject.Inject

class FakeClassroomRepository @Inject constructor() : ClassroomRepositoryInterface {

    companion object {
        val CLASSROOM_LIST = listOf(
            Classroom(
                id = "CM3",
            ),
            Classroom(
                id = "CE-1515",
            ),
            Classroom(
                id = "AAC 2 31",
            ),
            Classroom(
                id = "ELA 2",
            )
        )
    }


    override suspend fun getClassrooms(): List<Classroom> = CLASSROOM_LIST

    override suspend fun getRoomById(id: String): Classroom = Classroom(id = "CM3")
}