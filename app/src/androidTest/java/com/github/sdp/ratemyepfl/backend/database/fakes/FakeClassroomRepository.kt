package com.github.sdp.ratemyepfl.backend.database.fakes

import com.github.sdp.ratemyepfl.backend.database.LoaderRepository
import com.github.sdp.ratemyepfl.backend.database.firebase.reviewable.ClassroomRepositoryImpl
import com.github.sdp.ratemyepfl.backend.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.backend.database.reviewable.ReviewableRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import javax.inject.Inject

class FakeClassroomRepository @Inject constructor(
    val repository: FakeLoaderRepository<Classroom>
) :
    ClassroomRepository,
    ReviewableRepository<Classroom>, LoaderRepository<Classroom> by repository {

    override val offlineData: List<Classroom> = ClassroomRepositoryImpl.OFFLINE_CLASSROOMS

    init {
        repository.elements = CLASSROOM_LIST.toSet()
    }

    companion object {
        private val baseRoom = Classroom("name", 2.5, 1, "roomKind")

        val CLASSROOM_LIST = listOf(
            baseRoom.copy(
                name = "CM3"
            ),
            baseRoom.copy(
                name = "CE-1515"
            ),
            baseRoom.copy(
                name = "AAC 2 31"
            ),
            baseRoom.copy(
                name = "ELA 2"
            )
        )

        val ROOM_NO_REVIEW = baseRoom.copy(grade = 0.0, numReviews = 0)
        val ROOM_WITH_REVIEW = baseRoom.copy(grade = 5.5, numReviews = 3)

        var roomById = baseRoom.copy(name = "CM3")
    }


    override suspend fun getClassrooms(): List<Classroom> = CLASSROOM_LIST

    override suspend fun getRoomById(id: String): Classroom = roomById

}