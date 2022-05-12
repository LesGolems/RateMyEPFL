package com.github.sdp.ratemyepfl.database.fakes

import com.github.sdp.ratemyepfl.database.query.QueryResult
import com.github.sdp.ratemyepfl.database.query.QueryState
import com.github.sdp.ratemyepfl.database.reviewable.ClassroomRepository
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.model.review.ReviewRating
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeClassroomRepository @Inject constructor() : ClassroomRepository {

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

    override fun search(prefix: String): QueryResult<List<Classroom>> = QueryResult(
        flow { emit(QueryState.success(listOf())) }
    )
}